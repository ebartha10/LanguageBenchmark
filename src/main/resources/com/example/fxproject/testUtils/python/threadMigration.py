import threading
import psutil
import os
import timeit
num_iterations = int(1e6)
def thread_task(proc):
    for i in range(num_iterations):
        proc.cpu_affinity([i%2])

if __name__ == '__main__':
    # Set CPU affinity to specific cores (e.g., cores 0 and 1)
    p = psutil.Process(os.getpid())
    p.cpu_affinity([0])
    # Create and start thread
    creation_time = timeit.timeit("""
thread = threading.Thread(target=thread_task(p))
thread.start()
thread.join()""", globals=globals(), number=1)
    print(creation_time)