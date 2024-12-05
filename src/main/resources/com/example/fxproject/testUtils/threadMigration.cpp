#include <iostream>
#include <chrono>
#include <thread>
#include <windows.h>
using namespace std;
volatile int sharedInt = 0;
const int NUM_THREADS = 1; // Number of threads to create
const int SWITCH_AMMOUNT = 1e4;
HANDLE mtx;
DWORD WINAPI threadFunction(LPVOID arg) {
    HANDLE threadHandle = GetCurrentThread();
    for (int i = 0; i < SWITCH_AMMOUNT; i++) {
        SetThreadAffinityMask(threadHandle, 1 << i % 2);
        SwitchToThread();
        Sleep(0);
    }
    return 0;
}

int main() {
    HANDLE threads[NUM_THREADS];
    mtx = CreateMutex(NULL, FALSE, NULL);
    if(mtx == nullptr) {
        perror("CreateMutex");
        return -1;
    }
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

    return duration.count() * 1000000000 / SWITCH_AMMOUNT;
}
