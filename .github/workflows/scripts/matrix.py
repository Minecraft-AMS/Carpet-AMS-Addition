import json
import os
import sys

def main():
    target_subproject = os.environ.get("TARGET_SUBPROJECT", "")

    with open("minecraftVersions.json", encoding="utf-8") as file:
        versions = json.load(file)["versions"]

    if target_subproject:
        if target_subproject not in versions:
            print(f"Unexpected subproject: {target_subproject}", file=sys.stderr)
            sys.exit(1)
        versions = [target_subproject]

    matrix = {
        "include": [
            {"subproject_dir": version}
            for version in versions
        ]
    }

    with open(os.environ["GITHUB_OUTPUT"], "a", encoding="utf-8") as file:
        file.write(f"matrix={json.dumps(matrix)}\n")

    print(json.dumps(matrix, indent=2))


if __name__ == "__main__":
    main()
