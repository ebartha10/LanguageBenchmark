import timeit
static_val = 42
other_val = 0
if __name__ == '__main__':
    access_time = timeit.timeit('other_val = static_val', number=int(1e8), globals=globals())
    print(int(access_time * 100000))
