#include <iostream>
#include <chrono>

const int SIZE = 1e4; // Size of the static array
int f = 42;
int main() {

    // Measure the time taken to access the static memory

    auto start = std::chrono::high_resolution_clock::now();
    for (int i = 0; i < SIZE; ++i) {
        f = 1;
    }
    auto end = std::chrono::high_resolution_clock::now();

    // Calculate the duration
    std::chrono::duration<double> duration = end - start;
    return duration.count() * 1000000000 / SIZE;
}
