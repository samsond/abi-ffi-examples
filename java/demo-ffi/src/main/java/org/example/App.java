package org.example;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {

        String libraryPath = System.getProperty("java.library.path");

        if (libraryPath == null) {
            System.err.println("Library path not set!");
            return;
        }


        try {
            try (Arena arena = Arena.ofConfined()) {
                // Load the library
                SymbolLookup lib = SymbolLookup.libraryLookup(libraryPath, arena);

                // Find the binary_search function
                MemorySegment binarySearchSymbol = lib.find("binary_search").orElseThrow();

                // Prepare the function descriptor
                FunctionDescriptor descriptor = FunctionDescriptor.of(
                        ValueLayout.JAVA_INT,   // return type
                        ValueLayout.ADDRESS,    // arr type
                        ValueLayout.JAVA_INT,   // len type
                        ValueLayout.JAVA_INT    // target type
                );

                // Create a MethodHandle for calling the binary_search function
                MethodHandle methodHandle = Linker.nativeLinker().downcallHandle(binarySearchSymbol, descriptor);

                // Prepare arguments
                int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
                int len = arr.length;
                int target = 7;

                // Allocate off-heap memory and copy the array data
                MemorySegment arrSegment = arena.allocateArray(ValueLayout.JAVA_INT, arr);

                // Call the binary_search function
                int result = (int) methodHandle.invokeExact(arrSegment, len, target);

                System.out.println("Result: " + result);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Error loading library: " + e.getMessage());
            e.printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
