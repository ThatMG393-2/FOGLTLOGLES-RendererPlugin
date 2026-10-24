#!/usr/bin/env bash
# Fetch prebuilt FOGLTLOGLES .so files from upstream CI into app/src/main/jniLibs.
#
# Usage:  scripts/fetch-fogle-libs.sh [RUN_ID]
#   RUN_ID  optional upstream run id; default = newest usable run of debug.yml
#
# Needs: gh (GitHub CLI, authenticated: `gh auth login` or GH_TOKEN), python3
set -euo pipefail

UPSTREAM="${FOGLE_REPO:-ThatMG393-2/FOGLTLOGLES}"
WORKFLOW="${FOGLE_WORKFLOW:-debug.yml}"
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
OUT="$ROOT/app/src/main/jniLibs"

# abi : artifact name (spelling matches upstream's workflow)
declare -A ARTIFACTS=(
  [arm64-v8a]="FOGLTOGLES - 64-bit"
  [armeabi-v7a]="FOGLTOGLES - 32-bit"
)

has_all_artifacts() { # $1 = run id
  gh api "repos/$UPSTREAM/actions/runs/$1/artifacts?per_page=100" \
    --jq '[.artifacts[] | select(.expired == false) | .name]' |
  python3 -c '
import sys, json
have = set(json.load(sys.stdin))
sys.exit(0 if set(sys.argv[1:]) <= have else 1)' "${ARTIFACTS[@]}"
}

RUN_ID="${1:-}"
if [ -z "$RUN_ID" ]; then
  # Upstream triggers us from inside its own run, so that run may still be
  # "in_progress" (its artifacts are already uploaded). Accept any run that
  # did not fail/cancel and that has both artifacts available.
  for id in $(gh run list -R "$UPSTREAM" -w "$WORKFLOW" -L 15 \
      --json databaseId,conclusion --jq '.[] | select(.conclusion != "failure" and .conclusion != "cancelled" and .conclusion != "skipped") | .databaseId'); do
    if has_all_artifacts "$id"; then RUN_ID="$id"; break; fi
  done
fi
[ -n "$RUN_ID" ] || { echo "No usable upstream run found" >&2; exit 1; }

SHA="$(gh run view "$RUN_ID" -R "$UPSTREAM" --json headSha --jq .headSha)"
echo "Using $UPSTREAM run $RUN_ID (commit $SHA)"

rm -rf "$OUT"
for abi in "${!ARTIFACTS[@]}"; do
  mkdir -p "$OUT/$abi"
  gh run download "$RUN_ID" -R "$UPSTREAM" -n "${ARTIFACTS[$abi]}" -D "$OUT/$abi"
  ls "$OUT/$abi"/*.so >/dev/null  # fail if no .so landed
done

printf 'repo=%s\nrun=%s\ncommit=%s\n' "$UPSTREAM" "$RUN_ID" "$SHA" > "$OUT/FOGLE_VERSION.txt"
echo "Done:"; ls -l "$OUT"/*/
