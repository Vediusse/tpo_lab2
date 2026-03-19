import numpy as np
import matplotlib.pyplot as plt


def system_function(x: np.ndarray) -> np.ndarray:
    y = np.full_like(x, np.nan, dtype=float)

    # x <= 0
    left = x <= 0
    if np.any(left):
        xl = x[left]
        sin_x = np.sin(xl)
        cos_x = np.cos(xl)
        tan_x = np.tan(xl)

        sec_x = 1.0 / cos_x
        csc_x = 1.0 / sin_x

        numerator = ((tan_x ** 2) * tan_x + cos_x) * sec_x
        denominator = (csc_x * tan_x) - tan_x

        with np.errstate(divide="ignore", invalid="ignore"):
            y[left] = (numerator / denominator) - cos_x

    # x > 0
    right = x > 0
    if np.any(right):
        xr = x[right]

        log2_x = np.log(xr) / np.log(2)
        log3_x = np.log(xr) / np.log(3)
        log5_x = np.log(xr) / np.log(5)
        ln_x = np.log(xr)

        with np.errstate(divide="ignore", invalid="ignore"):
            y[right] = ((((log2_x ** 3) ** 2) + log2_x) ** 2) - (
                log5_x + (ln_x * ((log3_x ** 2) + (log2_x ** 3)))
            )

    # убираем бесконечности, чтобы график не соединял их линиями
    y[~np.isfinite(y)] = np.nan
    return y


# диапазон по x
x = np.linspace(-10, 10, 200000)
y = system_function(x)

plt.figure(figsize=(12, 6))
plt.plot(x, y)

plt.axhline(0)
plt.axvline(0)
plt.ylim(-50, 50)  # можно менять для удобства просмотра
plt.xlim(-10, 10)

plt.title("Graph of the function system")
plt.xlabel("x")
plt.ylabel("y")
plt.grid(True)

plt.show()