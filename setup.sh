set -e

git submodule update --init --recursive --depth=1
./FOGLTLOGLES/deps/shaderc/utils/git-sync-deps

echo "Done."