import matplotlib.pyplot as plt
import numpy as np


history_stamina = []
history_efficiency = []

for n in np.arange(1.0, 0.0, -0.01):
    history_stamina.append(n)
    history_efficiency.append(1 - (1 - n) ** 2)

plt.title('Actor stamina and its efficiency correlation')
plt.xlabel('Stamina')
plt.ylabel('Efficiency')
plt.plot(history_stamina, history_efficiency)
plt.gca().invert_xaxis()
plt.show()
