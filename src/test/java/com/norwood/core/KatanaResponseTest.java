package com.norwood.core;

import junit.framework.TestCase;

public class KatanaResponseTest extends TestCase {
    public void testSuccessAndError() {
        KatanaResponse success = KatanaResponse.success(123);
        assertEquals("123", success.value());
        KatanaResponse err = KatanaResponse.error("fail");
        assertEquals("fail", err.value());
    }
}
