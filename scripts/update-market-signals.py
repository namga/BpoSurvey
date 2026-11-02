#!/usr/bin/env python3
"""Fetch public Google News RSS results for BPO market and recruitment intelligence."""
from __future__ import annotations
import json, sys, time, urllib.parse, urllib.request, urllib.error, xml.etree.ElementTree as ET
from datetime import datetime, timezone
from email.utils import parsedate_to_datetime
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
DATA = ROOT / "web" / "data"
MARKET_OUTPUT = DATA / "market-signals.json"
RECRUITMENT_OUTPUT = DATA / "recruitment-signals.json"
MARKET_QUERIES = [
    ("BPO / outsourcing", 'BPO Vietnam outsourcing'),
    ("Contact center / CSKH", 'contact center call center Vietnam tuyển dụng'),
    ("Tuyển dụng / lao động", 'tuyển dụng lao động kho sản xuất Việt Nam'),
    ("Nhân sự / tiền lương", 'tiền lương thị trường lao động Việt Nam'),
    ("Công ty / dự án mới", 'outsourcing BPO logistics customer service Vietnam hiring'),
]
RECRUITMENT_QUERIES = [
    ("BPO / Contact Center", 'BPO contact center call center hiring tuyển dụng Vietnam'),
    ("Kho vận / Logistics", 'warehouse logistics kho vận hiring tuyển dụng Vietnam'),
    ("Sản xuất / Nhân sự số lượng lớn", 'tuyển công nhân tuyển dụng số lượng lớn Vietnam'),
    ("Chăm sóc khách hàng / Sales", 'customer service telesales chăm sóc khách hàng sales tuyển dụng Vietnam'),
    ("Lương / Phúc lợi", 'mức lương salary BPO logistics warehouse customer service Vietnam'),
]

def iso_now() -> str:
    return datetime.now(timezone.utc).isoformat(timespec="seconds").replace("+00:00", "Z")
def parse_date(raw: str) -> str:
    try:
        dt = parsedate_to_datetime(raw)
        if dt.tzinfo is None: dt = dt.replace(tzinfo=timezone.utc)
        return dt.astimezone(timezone.utc).isoformat(timespec="seconds").replace("+00:00", "Z")
    except Exception: return ""
def clean(text: str) -> str: return " ".join((text or "").split())
def fetch(query: str, topic: str, category: str) -> list[dict]:
    params = urllib.parse.urlencode({"q": query, "hl": "vi", "gl": "VN", "ceid": "VN:vi"})
    url = "https://news.google.com/rss/search?" + params
    req = urllib.request.Request(url, headers={"User-Agent": "BPO-Intelligence-Hub-Public-RSS/2.0"})
    payload = b""
    last_error = None
    for attempt in range(3):
        try:
            with urllib.request.urlopen(req, timeout=30) as response:
                status = getattr(response, "status", 200)
                payload = response.read()
            if status != 200:
                raise RuntimeError(f"HTTP {status}")
            break
        except Exception as exc:
            last_error = exc
            if attempt < 2:
                time.sleep(1.5 * (attempt + 1))
    if not payload:
        raise RuntimeError(f"RSS request failed after retries: {last_error}")
    root = ET.fromstring(payload); items = []
    nodes = root.findall("./channel/item")
    print(f"RSS response: HTTP 200, {len(nodes)} raw entries for query: {query}")
    for node in nodes:
        title, link = clean(node.findtext("title")), clean(node.findtext("link"))
        if not title or not link: continue
        source_node = node.find("source")
        items.append({"title": title, "url": link, "source": clean(source_node.text if source_node is not None else "Google News RSS") or "Google News RSS", "published": parse_date(clean(node.findtext("pubDate"))), "topic": topic, "category": category, "verificationStatus": "Cần xác minh"})
    return items

def dedupe_sort(items: list[dict], limit: int) -> list[dict]:
    unique = {}
    for item in items:
        key = item["url"].split("&utm_")[0]
        if key not in unique: unique[key] = item
    ordered = list(unique.values()); ordered.sort(key=lambda x: x.get("published", ""), reverse=True)
    return ordered[:limit]
def load_previous(path: Path) -> dict:
    try:
        value = json.loads(path.read_text(encoding="utf-8"))
        return value if isinstance(value, dict) else {}
    except Exception: return {}
def write_feed(path: Path, feed_name: str, provider: str, items: list[dict], errors: list[str], notice: str) -> None:
    payload = {"schemaVersion": 2, "feed": feed_name, "updatedAt": iso_now(), "provider": provider, "itemCount": len(items), "items": items, "sourceErrors": errors, "notice": notice}
    path.write_text(json.dumps(payload, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"Wrote {len(items)} {feed_name} items to {path}")
def main() -> int:
    DATA.mkdir(parents=True, exist_ok=True)
    failures: list[str] = []
    results: dict[str, list[dict]] = {"market": [], "recruitment": []}
    for kind, queries in (("market", MARKET_QUERIES), ("recruitment", RECRUITMENT_QUERIES)):
        for topic, query in queries:
            try:
                batch = fetch(query, topic, "recruitment" if kind == "recruitment" else "market")
                results[kind].extend(batch[:20]); print(f"{kind} · {topic}: {len(batch)} RSS entries")
            except Exception as exc:
                message = f"{kind} / {topic}: {type(exc).__name__}: {exc}"
                failures.append(message); print("WARN " + message, file=sys.stderr)
            time.sleep(0.25)
    market_items = dedupe_sort(results["market"], 60)
    recruitment_items = dedupe_sort(results["recruitment"], 80)
    # Preserve the last successful feed if every query for that feed fails.
    if not market_items and not recruitment_items:
        print("ERROR: all public RSS queries failed; preserving existing feeds.", file=sys.stderr); return 1
    notice = "RSS headline metadata from public search results only. This feed does not crawl LinkedIn or verify job availability, salary, employer identity, or article contents. Open the original source before using a signal."
    if market_items:
        write_feed(MARKET_OUTPUT, "market", "Google News RSS", market_items, failures, notice)
    else:
        previous = load_previous(MARKET_OUTPUT)
        if isinstance(previous.get("items"), list) and previous.get("items"):
            print("WARN: keeping previous market feed because this run returned no market items.", file=sys.stderr)
        else:
            write_feed(MARKET_OUTPUT, "market", "Google News RSS", [], failures, notice)
    if recruitment_items:
        write_feed(RECRUITMENT_OUTPUT, "recruitment", "Google News RSS", recruitment_items, failures, notice)
    else:
        previous = load_previous(RECRUITMENT_OUTPUT)
        if isinstance(previous.get("items"), list) and previous.get("items"):
            print("WARN: keeping previous recruitment feed because this run returned no recruitment items.", file=sys.stderr)
        else:
            write_feed(RECRUITMENT_OUTPUT, "recruitment", "Google News RSS", [], failures, notice)
    if failures: print(f"Completed with {len(failures)} source-query warning(s).", file=sys.stderr)
    return 0
if __name__ == "__main__": raise SystemExit(main())
