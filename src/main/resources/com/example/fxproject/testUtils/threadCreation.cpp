#include <iostream>
#include <chrono>
#include <thread>
#include <windows.h>
using namespace std;
DWORD WINAPI threadFunction(LPVOID arg) {
    return 0;
}

const int NUM_THREADS = 1e4; // Number of threads to create
int main() {
    HANDLE threads[NUM_THREADS];

    // Measure the time taken to create threads
    auto start = std::chrono::high_resolution_clock::now();

    for (int i = 0; i < NUM_THREADS; ++i) {
        threads[i] = CreateThread(NULL, 0, threadFunction, NULL, 0, NULL);
    }
    // Wait for all threads to finish
    WaitForMultipleObjects(NUM_THREADS, threads, TRUE, INFINITE);

    auto end = std::chrono::high_resolution_clock::now();

    // Close thread handles
    for (int i = 0; i < NUM_THREADS; ++i) {
        CloseHandle(threads[i]);
    }
    // Calculate the duration
    std::chrono::duration<double> duration = end - start;
    std::cout << duration.count() * 100000 << '\n';
    return duration.count() * 100000;
}
