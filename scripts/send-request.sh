#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"
BASE_URL="${1:-http://localhost:8080}"
HTTP_FILE="${2:-$ROOT/src/test/resources/ping.http}"

if [[ ! -f "$HTTP_FILE" ]]; then
  echo "Request file not found: $HTTP_FILE" >&2
  echo "Usage: $0 [base_url] [http_file]" >&2
  exit 1
fi

# Parse request line and headers until the blank line; remainder is the body
request_line=""
headers=()
body=""
in_headers=1

while IFS= read -r line || [[ -n "$line" ]]; do
  if [[ -z "$request_line" ]]; then
    request_line="$line"
    continue
  fi

  if [[ $in_headers -eq 1 ]]; then
    if [[ -z "$line" ]]; then
      in_headers=0
      continue
    fi
    headers+=("$line")
    continue
  fi

  if [[ -n "$body" ]]; then
    body+=$'\n'
  fi
  body+="$line"
done < "$HTTP_FILE"

method="${request_line%% *}"
path_and_rest="${request_line#* }"
path="${path_and_rest%% *}"

curl_args=(-sS -i -X "$method" "${BASE_URL}${path}")

for header in "${headers[@]}"; do
  name="${header%%: *}"
  # Let curl set Host and Content-Length for the local target
  lower_name="$(printf '%s' "$name" | tr '[:upper:]' '[:lower:]')"
  case "$lower_name" in
    host|content-length|accept-encoding) continue ;;
  esac
  curl_args+=(-H "$header")
done

curl_args+=(--data-binary "$body")

exec curl "${curl_args[@]}"
