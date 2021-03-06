package org.springside.modules.utils.text;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springside.modules.utils.base.annotation.Nullable;
import org.springside.modules.utils.collection.ListUtil;

import com.google.common.base.Utf8;

/**
 * 尽量使用Common Lang StringUtils, 完全覆盖了guva对应的部分(除了split和join的API没那么酷)
 * 
 * 本类仅补充少量额外方法.
 * 
 * @author calvin
 */
public abstract class MoreStringUtil {

	/**
	 * 高性能的Split，针对char的分隔符号，比JDK String自带的高效.
	 * 
	 * from Commons Lange 3.5 StringUtils, 做如下优化:
	 * 
	 * 1. 最后不做数组转换，直接返回List.
	 * 
	 * 2. 可设定List初始大小.
	 * 
	 * 3. preserveAllTokens 取默认值false
	 * 
	 * @return 如果为null返回null, 如果为""返回空数组
	 */
	public static List<String> split(@Nullable final String str, final char separatorChar, int expectParts) {
		if (str == null) {
			return null;
		}
		final int len = str.length();
		if (len == 0) {
			return ListUtil.emptyList();
		}
		final List<String> list = new ArrayList<String>(expectParts);
		int i = 0;
		int start = 0;
		boolean match = false;
		while (i < len) {
			if (str.charAt(i) == separatorChar) {
				if (match) {
					list.add(str.substring(start, i));
					match = false;
				}
				start = ++i;
				continue;
			}
			match = true;
			i++;
		}
		if (match) {
			list.add(str.substring(start, i));
		}
		return list;
	}

	/**
	 * String 有replace(char,char)，但缺少单独replace first/last的
	 */
	public static String replaceFirst(@Nullable String s, char sub, char with) {
		if (s == null) {
			return null;
		}
		int index = s.indexOf(sub);
		if (index == -1) {
			return s;
		}
		char[] str = s.toCharArray();
		str[index] = with;
		return new String(str);
	}

	/**
	 * String 有replace(char,char)替换全部char，但缺少单独replace first/last
	 */
	public static String replaceLast(@Nullable String s, char sub, char with) {
		if (s == null) {
			return null;
		}

		int index = s.lastIndexOf(sub);
		if (index == -1) {
			return s;
		}
		char[] str = s.toCharArray();
		str[index] = with;
		return new String(str);
	}

	/**
	 * 判断字符串是否以字母开头
	 * 
	 * 如果字符串为Null或空，返回false
	 */
	public static boolean startWith(@Nullable CharSequence s, char c) {
		if (StringUtils.isEmpty(s)) {
			return false;
		}
		return s.charAt(0) == c;
	}

	/**
	 * 判断字符串是否以字母结尾
	 * 
	 * 如果字符串为Null或空，返回false
	 */
	public static boolean endWith(@Nullable CharSequence s, char c) {
		if (StringUtils.isEmpty(s)) {
			return false;
		}
		return s.charAt(s.length() - 1) == c;
	}

	/**
	 * 计算字符串被UTF8编码后的字节数 via guava
	 * 
	 * @see Utf8#encodedLength(CharSequence)
	 */
	public static int utf8EncodedLength(@Nullable CharSequence sequence) {
		if (StringUtils.isEmpty(sequence)) {
			return 0;
		}
		return Utf8.encodedLength(sequence);
	}
}
