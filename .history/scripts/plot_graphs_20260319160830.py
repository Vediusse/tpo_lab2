#!/usr/bin/env python3
"""Build comparison plots for analytical and approximated function values."""

from __future__ import annotations

import argparse
import math
from pathlib import Path

import matplotlib.pyplot as plt
import numpy as np
import pandas as pd


STANDARD_STEMS = (
    "sin",
    "ln",
    "log2",
    "log3",
    "log5",
    "cos",
    "tan",
    "cot",
    "sec",
    "csc",
)

CSV_CANDIDATES = {
    "sin": ("sin.csv", "trig-sin.csv"),
    "ln": ("ln.csv",),
    "log2": ("log2.csv",),
    "log3": ("log3.csv",),
    "log5": ("log5.csv",),
    "cos": ("cos.csv", "trig-cos.csv"),
    "tan": ("tan.csv", "trig-tan.csv"),
    "cot": ("cot.csv",),
    "sec": ("sec.csv",),
    "csc": ("csc.csv",),
    "system_left": ("system_left.csv",),
    "system_right": ("system_right.csv",),
    "system": ("system.csv",),
}

ANALYTICAL_GRID_POINTS = 2000


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description="Plot analytical and approximated curves from CSV files."
    )
    parser.add_argument("--input-dir", default="output", help="Directory with CSV files.")
    parser.add_argument("--output-dir", default="plots", help="Directory for PNG plots.")
    return parser.parse_args()


def load_csv(csv_path: Path) -> pd.DataFrame:
    """Read a CSV file with columns x,result."""
    frame = pd.read_csv(csv_path)
    required = {"x", "result"}
    if not required.issubset(frame.columns):
        raise ValueError(f"CSV {csv_path} must contain columns x,result")

    frame = frame.copy()
    frame["x"] = pd.to_numeric(frame["x"], errors="coerce")
    frame["result"] = pd.to_numeric(frame["result"], errors="coerce")
    frame["result"] = frame["result"].replace([float("inf"), float("-inf")], np.nan)
    return frame.sort_values("x")


def resolve_csv_path(input_dir: Path, stem: str) -> Path | None:
    """Return the first existing CSV path for the requested function."""
    for candidate in CSV_CANDIDATES.get(stem, (f"{stem}.csv",)):
        path = input_dir / candidate
        if path.exists():
            return path
    return None


def series_kind(input_dir: Path, csv_path: Path) -> str:
    """Detect whether the input represents a table stub or production approximation."""
    parts = {part.lower() for part in input_dir.parts}
    if "tables" in parts or csv_path.name.startswith("trig-"):
        return "table stub"
    return "approximation"


def analytical_function(stem: str, x_values: np.ndarray) -> np.ndarray:
    """Return analytical values for a supported function."""
    if stem == "sin":
        return np.sin(x_values)
    if stem == "ln":
        return np.where(x_values > 0.0, np.log(x_values), np.nan)
    if stem == "log2":
        return np.where(x_values > 0.0, np.log(x_values) / math.log(2.0), np.nan)
    if stem == "log3":
        return np.where(x_values > 0.0, np.log(x_values) / math.log(3.0), np.nan)
    if stem == "log5":
        return np.where(x_values > 0.0, np.log(x_values) / math.log(5.0), np.nan)
    if stem == "cos":
        return np.cos(x_values)
    if stem == "tan":
        cos_values = np.cos(x_values)
        return np.where(np.abs(cos_values) < 1e-10, np.nan, np.tan(x_values))
    if stem == "cot":
        sin_values = np.sin(x_values)
        return np.where(np.abs(sin_values) < 1e-10, np.nan, np.cos(x_values) / sin_values)
    if stem == "sec":
        cos_values = np.cos(x_values)
        return np.where(np.abs(cos_values) < 1e-10, np.nan, 1.0 / cos_values)
    if stem == "csc":
        sin_values = np.sin(x_values)
        return np.where(np.abs(sin_values) < 1e-10, np.nan, 1.0 / sin_values)
    if stem == "system":
        return analytical_system(x_values)
    raise ValueError(f"Unsupported analytical function: {stem}")


def analytical_grid(frame: pd.DataFrame, stem: str) -> tuple[np.ndarray, np.ndarray]:
    """Build a dense analytical curve independently from CSV sample nodes."""
    finite_x = frame["x"].to_numpy(dtype=float)
    finite_x = finite_x[np.isfinite(finite_x)]
    if finite_x.size == 0:
        return np.array([]), np.array([])

    min_x = np.min(finite_x)
    max_x = np.max(finite_x)
    if math.isclose(min_x, max_x):
        x_values = np.array([min_x], dtype=float)
    else:
        x_values = np.linspace(min_x, max_x, ANALYTICAL_GRID_POINTS)
    return x_values, analytical_function(stem, x_values)


def safe_reciprocal(values: np.ndarray, threshold: float = 1e-10) -> np.ndarray:
    """Return 1/x with NaN near zero to avoid warnings and ugly spikes."""
    result = np.full_like(values, np.nan, dtype=float)
    mask = np.abs(values) >= threshold
    result[mask] = 1.0 / values[mask]
    return result


def analytical_system(x_values: np.ndarray) -> np.ndarray:
    """Return analytical values for the full piecewise system."""
    result = np.full_like(x_values, np.nan, dtype=float)

    left_mask = x_values <= 0.0
    left_x = x_values[left_mask]
    if left_x.size:
        sin_x = np.sin(left_x)
        cos_x = np.cos(left_x)
        tan_x = np.tan(left_x)
        sec_x = safe_reciprocal(cos_x)
        csc_x = safe_reciprocal(sin_x)
        denominator = csc_x * tan_x - tan_x
        left_value = ((((tan_x ** 2) * tan_x + cos_x) * sec_x) / denominator) - cos_x
        left_value = np.where(np.abs(denominator) < 1e-10, np.nan, left_value)
        result[left_mask] = np.where(np.isfinite(left_value), left_value, np.nan)

    right_mask = x_values > 0.0
    right_x = x_values[right_mask]
    if right_x.size:
        ln_x = np.log(right_x)
        log2_x = ln_x / math.log(2.0)
        log3_x = ln_x / math.log(3.0)
        log5_x = ln_x / math.log(5.0)
        log2_cube = log2_x ** 3
        right_value = ((log2_cube ** 2 + log2_x) ** 2) - (
            log5_x + (ln_x * ((log3_x ** 2) + log2_cube))
        )
        result[right_mask] = right_value

    return result


def apply_reasonable_limits(approx_values: np.ndarray, analytical_values: np.ndarray) -> None:
    """Limit giant asymptotic spikes so both curves remain visible."""
    merged = np.concatenate([approx_values, analytical_values])
    merged = merged[np.isfinite(merged)]
    if merged.size < 10:
        return

    lower = np.percentile(merged, 1)
    upper = np.percentile(merged, 99)
    if np.isfinite(lower) and np.isfinite(upper) and lower < upper:
        padding = (upper - lower) * 0.1
        plt.ylim(lower - padding, upper + padding)


def render_sample_series(
        x_values: np.ndarray,
        series_values: np.ndarray,
        label: str,
        kind: str
) -> None:
    """Render CSV data either as an approximation curve or as sparse table-stub points."""
    if kind == "table stub" or len(x_values) <= 32:
        plt.plot(
            x_values,
            series_values,
            color="#0f766e",
            linewidth=1.0,
            linestyle="--",
            alpha=0.75
        )
        plt.scatter(x_values, series_values, color="#0f766e", s=22, label=label, zorder=3)
        return

    plt.plot(x_values, series_values, color="#0f766e", linewidth=1.8, alpha=0.85, label=label)


def plot_comparison(frame: pd.DataFrame, stem: str, png_path: Path, title: str, kind: str) -> None:
    """Plot analytical and approximated values on one chart."""
    sample_x = frame["x"].to_numpy(dtype=float)
    sample_values = frame["result"].to_numpy(dtype=float)
    analytical_x, analytical_values = analytical_grid(frame, stem)

    plt.figure(figsize=(11, 6))
    plt.plot(analytical_x, analytical_values, color="#2563eb", linewidth=1.4, label="Analytical")
    render_sample_series(sample_x, sample_values, kind.title(), kind)
    apply_reasonable_limits(sample_values, analytical_values)
    plt.title(title)
    plt.xlabel("x")
    plt.ylabel("result")
    plt.grid(True, linestyle="--", alpha=0.5)
    plt.legend()
    plt.tight_layout()
    plt.savefig(png_path, dpi=150)
    plt.close()

def plot_system_comparison(frame: pd.DataFrame, png_path: Path, title: str, kind: str) -> None:
    """Plot the system in two views: robust scale and a zoomed view."""
    sample_x = frame["x"].to_numpy(dtype=float)
    sample_values = frame["result"].to_numpy(dtype=float)
    analytical_x, analytical_values = analytical_grid(frame, "system")

    


    plt.sca(axes[1])
    plt.plot(analytical_x, analytical_values, color="#2563eb", linewidth=1.4, label="Analytical")
    render_sample_series(sample_x, sample_values, kind.title(), kind)
    plt.ylim(-8.0, 8.0)
    plt.xlabel("x")
    plt.ylabel("result")
    plt.grid(True, linestyle="--", alpha=0.5)
    plt.legend()

    plt.tight_layout()
    plt.savefig(png_path, dpi=150)
    plt.close()

def load_system_frame(input_dir: Path) -> pd.DataFrame:
    """Load full system CSV or assemble it from left/right branches."""
    merged_path = input_dir / "system.csv"
    if merged_path.exists():
        return load_csv(merged_path)

    frames = []
    for name in ("system_left.csv", "system_right.csv"):
        path = input_dir / name
        if path.exists():
            frames.append(load_csv(path))

    if not frames:
        raise FileNotFoundError("No system CSV files found")
    return pd.concat(frames, ignore_index=True).sort_values("x")


def plot_standard_functions(input_dir: Path, output_dir: Path) -> None:
    """Plot all standard functions if their CSV files exist."""
    for stem in STANDARD_STEMS:
        csv_path = resolve_csv_path(input_dir, stem)
        if csv_path is None:
            print(f"Skipping missing file: {input_dir / f'{stem}.csv'}")
            continue

        try:
            frame = load_csv(csv_path)
            png_path = output_dir / f"{stem}.png"
            kind = series_kind(input_dir, csv_path)
            plot_comparison(frame, stem, png_path, f"Analytical vs {kind} for {stem}", kind)
            print(f"Saved plot: {png_path}")
        except Exception as error:  # noqa: BLE001
            print(f"Failed to plot {csv_path}: {error}")


def plot_system_functions(input_dir: Path, output_dir: Path) -> None:
    """Plot left branch, right branch and the full combined system."""
    for stem in ("system_left", "system_right"):
        csv_path = resolve_csv_path(input_dir, stem)
        if csv_path is None:
            print(f"Skipping missing file: {input_dir / f'{stem}.csv'}")
            continue

        try:
            frame = load_csv(csv_path)
            png_path = output_dir / f"{stem}.png"
            kind = series_kind(input_dir, csv_path)
            plot_system_comparison(frame, png_path, f"Analytical vs {kind} for {stem}", kind)
            print(f"Saved plot: {png_path}")
        except Exception as error:  # noqa: BLE001
            print(f"Failed to plot {csv_path}: {error}")

    try:
        frame = load_system_frame(input_dir)
        png_path = output_dir / "system.png"
        system_path = resolve_csv_path(input_dir, "system") or resolve_csv_path(input_dir, "system_left")
        kind = series_kind(input_dir, system_path) if system_path is not None else "approximation"
        plot_system_comparison(frame, png_path, f"Analytical vs {kind} for system", kind)
        print(f"Saved plot: {png_path}")
    except Exception as error:  # noqa: BLE001
        print(f"Failed to plot system: {error}")


def main() -> int:
    args = parse_args()
    input_dir = Path(args.input_dir)
    output_dir = Path(args.output_dir)
    output_dir.mkdir(parents=True, exist_ok=True)

    print(f"Reading CSV files from: {input_dir.resolve()}")
    print(f"Saving PNG files to: {output_dir.resolve()}")

    plot_standard_functions(input_dir, output_dir)
    plot_system_functions(input_dir, output_dir)

    print("Done.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
