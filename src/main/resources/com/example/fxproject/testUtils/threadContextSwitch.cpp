#include <iostream>
#include <chrono>
#include <thread>
#include <windows.h>

int sharedInt = 0;
const int NUM_THREADS = 2;
const int SWITCH_AMOUNT = 1e5;
CRITICAL_SECTION cs;

DWORD WINAPI threadFunction(LPVOID arg) {
    for (int i = 0; i < SWITCH_AMOUNT; i++) {
        EnterCriticalSection(&cs);
        sharedInt++;
        LeaveCriticalSection(&cs);
    }
    return 0;
}

int main() {
    HANDLE threads[NUM_THREADS];
    InitializeCriticalSection(&cs);

    auto start = std::chrono::high_resolution_clock::now();

    for (int i = 0; i < NUM_THREADS; ++i) {
        threads[i] = CreateThread(NULL, 0, threadFunction, NULL, 0, NULL);
    }

    WaitForMultipleObjects(NUM_THREADS, threads, TRUE, INFINITE);

    auto end = std::chrono::high_resolution_clock::now();

    for (int i = 0; i < NUM_THREADS; ++i) {
        CloseHandle(threads[i]);
    }

    DeleteCriticalSection(&cs);

    std::chrono::duration<double> duration = end - start;

    return duration.count() * 1000000000 / SWITCH_AMOUNT;
}
