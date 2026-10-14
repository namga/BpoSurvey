#!/usr/bin/env python3
"""Fetch public Google News RSS headlines and write a dated market-signal feed."""
from __future__ import annotations
import json
import os
import sys
import time
import urllib.parse
import urllib.request
import xml.etree.ElementTree as ET
from datetime import datetime, timezone
from email.utils import parsedate_to_datetime
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
OUTPUT = ROOT / "web" / "data" / "market-signals.json"
QUERIES = [
    ("BPO / outsourcing", '("BPO" OR "business process outsourcing" OR "dịch vụ thuê ngoài") Vietnam'),
    ("Contact center / CSKH", '("contact center" OR "call center" OR "chăm sóc khách hàng") Vietnam tuyển dụng'),
    ("Tuyển dụng / lao động", '(tuyển dụng OR tuyển công nhân OR lao động) (kho OR sản xuất OR vệ sinh OR bảo vệ) Vietnam'),
    ("Nhân sự / tiền lương", '(tiền lương OR lương tối thiểu OR thị trường lao động) Việt Nam'),
    ("Công ty / dự án mới", '(outsourcing OR BPO OR logistics OR customer service) Vietnam investment OR hiring'),
]


def parse_date(raw: str) -> str:
    if not raw:
        return ""
    try:
        dt = parsedate_to_datetime(raw)
        if dt.tzinfo is None:
            dt = dt.replace(tzinfo=timezone.utc)
        return dt.astimezone(timezone.utc).isoformat(timespec="seconds").replace("+00:00", "Z")
    except Exception:
        return ""


def clean(text: str) -> str:
    return " ".join((text or "").split())


def fetch(query: str, topic: str) -> list[dict]:
    params = urllib.parse.urlencode({"q": query, "hl": "vi", "gl": "VN", "ceid": "VN:vi"})
    url = "https://news.google.com/rss/search?" + params
    req = urllib.request.Request(url, headers={"User-Agent": "BPO-Intelligence-Hub-MarketSignals/1.0 (public RSS reader)"})
    with urllib.request.urlopen(req, timeout=25) as response:
        payload = response.read()
    root = ET.fromstring(payload)
    items = []
    for node in root.findall("./channel/item"):
        title = clean(node.findtext("title"))
        link = clean(node.findtext("link"))
        source_node = node.find("source")
        source = clean(source_node.text if source_node is not None else "Google News RSS")
        published = parse_date(clean(node.findtext("pubDate")))
        if not title or not link:
            continue
        items.append({"title": title, "url": link, "source": source or "Google News RSS", "published": published, "topic": topic})
    return items


def main() -> int:
    OUTPUT.parent.mkdir(parents=True, exist_ok=True)
    collected: list[dict] = []
    errors: list[str] = []
    for topic, query in QUERIES:
        try:
            batch = fetch(query, topic)
            collected.extend(batch[:20])
            print(f"{topic}: {len(batch)} RSS entries")
        except Exception as exc:
            errors.append(f"{topic}: {exc}")
            print(f"WARN {topic}: {exc}", file=sys.stderr)
        time.sleep(0.4)

    unique: dict[str, dict] = {}
    for item in collected:
        key = item["url"].split("&utm_")[0]
        if key not in unique:
            unique[key] = item
    items = list(unique.values())
    items.sort(key=lambda x: x.get("published", ""), reverse=True)

    # Do not replace the existing feed with an empty file if every upstream source failed.
    if not items:
        print("ERROR: no RSS items received; preserving previous feed.", file=sys.stderr)
        return 1

    payload = {
        "schemaVersion": 1,
        "updatedAt": datetime.now(timezone.utc).isoformat(timespec="seconds").replace("+00:00", "Z"),
        "provider": "Google News RSS",
        "itemCount": min(len(items), 60),
        "items": items[:60],
        "sourceErrors": errors,
        "notice": "Headlines and links are collected from public RSS results; each article must be checked at its original source.",
    }
    OUTPUT.write_text(json.dumps(payload, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"Wrote {len(payload['items'])} market signals to {OUTPUT}")
    if errors:
        print(f"Completed with {len(errors)} feed query warning(s).", file=sys.stderr)
    return 0

if __name__ == "__main__":
    raise SystemExit(main())
