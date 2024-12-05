import timeit
import threading
def thread_task():
    return 0

if __name__ == '__main__':
    creation_time = timeit.timeit("""
thread = threading.Thread(target=thread_task)
thread.start()
thread.join()""", globals=globals(), number=int(1e2))
    print(int(creation_time * 1000000000 / int(1e2)))