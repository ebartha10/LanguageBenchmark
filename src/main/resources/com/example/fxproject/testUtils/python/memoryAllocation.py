import timeit
if __name__ == '__main__':
        # Measure the time taken to access and modify the memory
        start = timeit.default_timer()
        mem = list(range(int(1e4)))
        end = timeit.default_timer()
        # Calculate the duration
        duration = end - start
        print(int(duration * 1000000000 / int(1e4)))