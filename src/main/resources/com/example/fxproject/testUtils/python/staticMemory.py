import timeit
static_val = 42
other_val = 0
if __name__ == '__main__':
    access_time = timeit.timeit('other_val = static_val', number=int(1e4), globals=globals())
    print(access_time * 1000000000 / 1e4)
