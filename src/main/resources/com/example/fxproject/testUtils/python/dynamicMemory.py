import timeit

dynamic_list = list(range(1_000_000))

if __name__ == '__main__':
    access_time = timeit.timeit('for i in range(1_000_000): _ = dynamic_list[i]', globals=globals(), number=int(1))
    print(int(access_time * 100000))
