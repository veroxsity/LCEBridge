import json
import re
from pathlib import Path


MD_RULE_PATTERN = re.compile(r"\|\s*`([^`]+)`\s*\|[^|]*\|\s*`(\d+):(\d+)`\s*\|")


def parse_pattern(pattern: str):
    block_name = pattern
    required_props = {}

    lb = pattern.find("[")
    rb = pattern.rfind("]")
    if lb > 0 and rb > lb:
        block_name = pattern[:lb].strip()
        inside = pattern[lb + 1:rb].strip()
        if inside:
            for raw in inside.split(","):
                part = raw.strip()
                if not part or part == "*":
                    continue
                eq = part.find("=")
                if eq <= 0 or eq == len(part) - 1:
                    continue
                required_props[part[:eq].strip()] = part[eq + 1:].strip()

    return {
        "blockName": block_name,
        "requiredProps": required_props,
    }


def main():
    repo_root = Path(__file__).resolve().parents[1]
    md_path = repo_root / "core" / "src" / "main" / "resources" / "mappings" / "java_1.21.11_to_lce_complete_metadata_mapping.md"
    json_path = repo_root / "core" / "src" / "main" / "resources" / "mappings" / "block_rules.json"

    rules = []
    for line in md_path.read_text(encoding="utf-8").splitlines():
        match = MD_RULE_PATTERN.search(line)
        if not match:
            continue

        parsed = parse_pattern(match.group(1).strip())
        parsed["lceId"] = int(match.group(2))
        parsed["lceData"] = int(match.group(3))
        rules.append(parsed)

    json_path.write_text(json.dumps(rules, indent=2) + "\n", encoding="utf-8")
    print(f"Wrote {len(rules)} rules to {json_path}")


if __name__ == "__main__":
    main()
