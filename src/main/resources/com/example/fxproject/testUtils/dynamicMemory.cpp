#include <iostream>
#include <chrono>
using namespace std;
const int SIZE = (int)1e6; // Size of the static array
int *mem;

int main() {
    mem = static_cast<int *>(calloc(SIZE,sizeof(int)));
    *mem = 1;
    int temp = 0;
    auto start = std::chrono::high_resolution_clock::now();
    for (int i = 0; i < SIZE; ++i) {
            temp = mem[i]; // Access the static memory
    }
    auto end = std::chrono::high_resolution_clock::now();

    std::chrono::duration<double> duration = end - start;
    return duration.count() * 1000000000 / SIZE;
}
