#[no_mangle]
pub extern "C" fn binary_search(arr: *const i32, len: usize, target: i32) -> i32 {
    let slice = unsafe { std::slice::from_raw_parts(arr, len) };
    match slice.binary_search(&target) {
        Ok(index) => index as i32,
        Err(_) => -1,
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_binary_search() {
        let arr = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
        let len = arr.len();
        let target = 5;
        assert_eq!(binary_search(arr.as_ptr(), len, target), 4);
    }
}

