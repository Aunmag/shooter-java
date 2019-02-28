import matplotlib.pyplot as plt
import numpy as np


history_health = []
history_blackout = []

for health in np.arange(1.0, 0.0, -0.01):
    history_health.append(health)
    history_blackout.append((1 - health) ** 3.0)

plt.title('Player health and screen blackout correlation')
plt.xlabel('Health')
plt.ylabel('Wound (blackout)')
plt.plot(history_health, history_blackout)
plt.gca().invert_xaxis()
plt.show()
