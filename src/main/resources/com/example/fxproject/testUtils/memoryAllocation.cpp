#include <iostream>
#include <chrono>
using namespace std;
const int SIZE = 1e8; // Size of the static array
int *mem;
int main() {
    // Measure the time taken to access the static memory
    auto start = std::chrono::high_resolution_clock::now();
    mem = static_cast<int *>(calloc(SIZE, sizeof(int)));
    auto end = std::chrono::high_resolution_clock::now();
    // Calculate the duration
    std::chrono::duration<double> duration = end - start;

    return duration.count() * 100000;
}
