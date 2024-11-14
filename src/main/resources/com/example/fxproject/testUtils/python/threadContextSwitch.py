import threading
import time

# Shared counter and lock for simulating contention between threads
counter = 0
num_iterations = int(1e5)  # Number of increments by each thread
lock = threading.Lock()

# Worker function for each thread
def increment_counter():
    global counter
    for _ in range(num_iterations):
        with lock:
            counter += 1

def measure_context_switch_time():
    global counter
    counter = 0  # Reset counter

    # Create two threads that will contend for the counter
    thread1 = threading.Thread(target=increment_counter)
    thread2 = threading.Thread(target=increment_counter)

    # Start the timer
    start_time = time.perf_counter()

    # Start both threads
    thread1.start()
    thread2.start()

    # Wait for both threads to finish
    thread1.join()
    thread2.join()

    # Stop the timer
    end_time = time.perf_counter()

    # Calculate total time and average time per context switch
    total_time = end_time - start_time
    total_context_switches = num_iterations * 2  # Each thread completes `num_iterations` accesses
    average_context_switch_time = total_time / total_context_switches

    print(int(total_time * 100000))

# Run the context switch measurement
if __name__ == "__main__":
    measure_context_switch_time()
