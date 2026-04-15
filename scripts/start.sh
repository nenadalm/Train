#!/usr/bin/env bash

set -euo pipefail

script="$(realpath "${0}")"
script_path="$(dirname "${script}")"

natives_path="${script_path}/natives"
jar_path="${script_path}/Train.jar"

java -Djava.library.path="${natives_path}" -jar "${jar_path}"
