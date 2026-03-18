import json
from pathlib import Path


FALLBACK_LCE = (1 << 8) | 0


def normalize(value: str) -> str:
    out = value.strip()
    if len(out) >= 2 and out.startswith('"') and out.endswith('"'):
        out = out[1:-1]
    if len(out) > 1 and out[-1] in ("b", "s", "l", "f", "d") and out[-2].isdigit():
        out = out[:-1]
    return out


def load_rules(path: Path):
    rules = json.loads(path.read_text(encoding="utf-8"))
    normalized = []
    for rule in rules:
        normalized.append({
            "blockName": normalize(rule["blockName"]),
            "requiredProps": {normalize(k): normalize(v) for k, v in rule.get("requiredProps", {}).items()},
            "packed": ((int(rule["lceId"]) & 0xFF) << 8) | (int(rule["lceData"]) & 0xFF),
        })
    return normalized


def main():
    repo_root = Path(__file__).resolve().parents[1]
    reports_path = repo_root.parent / ".local" / "mc_reports" / "reports" / "blocks.json"
    rules_path = repo_root / "core" / "src" / "main" / "resources" / "mappings" / "block_rules.json"
    out_path = repo_root / "core" / "src" / "main" / "resources" / "mappings" / "block_state_mappings.json"

    blocks = json.loads(reports_path.read_text(encoding="utf-8"))
    rules = load_rules(rules_path)

    max_state = max(state["id"] for block in blocks.values() for state in block["states"])
    table = [FALLBACK_LCE] * (max_state + 1)

    matched = 0
    for block_name, block in blocks.items():
        normalized_name = normalize(block_name)
        for state in block["states"]:
            props = {normalize(k): normalize(str(v)) for k, v in state.get("properties", {}).items()}
            packed = FALLBACK_LCE
            for rule in rules:
                if rule["blockName"] != normalized_name:
                    continue
                req = rule["requiredProps"]
                if all(props.get(k) == v for k, v in req.items()):
                    packed = rule["packed"]
                    break
            table[state["id"]] = packed
            if packed != FALLBACK_LCE:
                matched += 1

    out_path.write_text(json.dumps(table, separators=(",", ":")) + "\n", encoding="utf-8")
    print(f"Wrote {len(table)} exact block state entries to {out_path}")
    print(f"Matched {matched} states, fallback {len(table) - matched}")


if __name__ == "__main__":
    main()
