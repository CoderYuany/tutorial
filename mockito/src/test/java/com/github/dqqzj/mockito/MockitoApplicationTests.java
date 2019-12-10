package com.github.dqqzj.mockito;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
class MockitoApplicationTests {

	/**
	 * 验证某些行为
	 */
	@Test
	void test0() {
		// mock creation 创建mock对象
		List mockedList = mock(List.class);

		//using mock object 使用mock对象
		mockedList.add("one");
		mockedList.clear();

		//verification 验证
		verify(mockedList).add("one");
		verify(mockedList).clear();
	}

	/**
	 * 如何做一些测试桩 (Stub)
	 */
	@Test
	void test1() {
		//You can mock concrete classes, not only interfaces
		// 你可以mock具体的类型,不仅只是接口
		LinkedList mockedList = mock(LinkedList.class);

		//stubbing
		// 测试桩
		when(mockedList.get(0)).thenReturn("first");
		when(mockedList.get(1)).thenThrow(new RuntimeException());

		//following prints "first"
		// 输出“first”
		System.out.println(mockedList.get(0));

		//following throws runtime exception
		// 抛出异常
		System.out.println(mockedList.get(1));

		//following prints "null" because get(999) was not stubbed
		// 因为get(999) 没有打桩，因此输出null
		System.out.println(mockedList.get(999));

		//Although it is possible to verify a stubbed invocation, usually it's just redundant
		//If your code cares what get(0) returns then something else breaks (often before even verify() gets executed).
		//If your code doesn't care what get(0) returns then it should not be stubbed. Not convinced? See here.
		// 验证get(0)被调用的次数
		verify(mockedList).get(0);
	}

	/**
	 * 参数匹配器 (matchers)
	 */
	@Test
	void test2() {
		LinkedList mockedList = mock(LinkedList.class);
		//stubbing using built-in anyInt() argument matcher
		// 使用内置的anyInt()参数匹配器
		when(mockedList.get(anyInt())).thenReturn("element");

		//stubbing using custom matcher (let's say isValid() returns your own matcher implementation):
		// 使用自定义的参数匹配器( 在isValid()函数中返回你自己的匹配器实现 )
		when(mockedList.contains(argThat((ArgumentMatcher) o -> o.equals("xx")))).thenReturn(true);

		//following prints "element"
		// 输出element
		System.out.println(mockedList.get(999));
		System.out.println(mockedList.contains("element"));
		//you can also verify using an argument matcher
		// 你也可以验证参数匹配器
		verify(mockedList).get(anyInt());
	}
}
