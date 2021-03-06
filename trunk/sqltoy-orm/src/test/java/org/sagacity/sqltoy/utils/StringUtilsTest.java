/**
 * 
 */
package org.sagacity.sqltoy.utils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;
import org.sagacity.sqltoy.SqlToyConstants;
import org.sagacity.sqltoy.utils.DataSourceUtils.DBType;

/**
 * @author zhongxuchen
 *
 */
public class StringUtilsTest {

	@Test
	public void testSplitExcludeSymMark1() {
		String source = "#[testNum],'#,#0.00'";
		String[] result = StringUtil.splitExcludeSymMark(source, ",", SqlToyConstants.filters);
		assertArrayEquals(result, new String[] { "#[testNum]", "'#,#0.00'" });
		source = ",'#,#0.00'";
		result = StringUtil.splitExcludeSymMark(source, ",", SqlToyConstants.filters);
		assertArrayEquals(result, new String[] { "", "'#,#0.00'" });

		source = "'\\'', t.`ORGAN_ID`, '\\''";
		result = StringUtil.splitExcludeSymMark(source, ",", SqlToyConstants.filters);
		assertArrayEquals(result, new String[] { "'\\''", " t.`ORGAN_ID`", " '\\''" });

		source = "orderNo,<td align=\"center\" rowspan=\"#[group('orderNo,').size()]\">,@dict(EC_PAY_TYPE,#[payType])</td>";
		result = StringUtil.splitExcludeSymMark(source, ",", SqlToyConstants.filters);
		assertArrayEquals(result,
				new String[] { "orderNo", "<td align=\"center\" rowspan=\"#[group('orderNo,').size()]\">",
						"@dict(EC_PAY_TYPE,#[payType])</td>" });
		source = "reportId=\"RPT_DEMO_005\",chart-index=\"1\",style=\"width:49%;height:350px;display:inline-block;\"";
		result = StringUtil.splitExcludeSymMark(source, ",", SqlToyConstants.filters);
		assertArrayEquals(result, new String[] { "reportId=\"RPT_DEMO_005\"", "chart-index=\"1\"",
				"style=\"width:49%;height:350px;display:inline-block;\"" });
		source = "a,\"\"\",\",a";
		result = StringUtil.splitExcludeSymMark(source, ",", SqlToyConstants.filters);
		for (String s : result) {
			System.err.println("[" + s.trim() + "]");
		}
		assertArrayEquals(result, new String[] { "a", "\"\"\",\"", "a" });
	}

	@Test
	public void testRegex() {
		String temp = "{Key}";
		String result = temp.replaceAll("(?i)\\$?\\{\\s*key\\s*\\}", "\\$\\{value\\}");
		System.err.println(result);
		System.err.println(result.replace("${value}", "chenren"));
	}

	@Test
	public void testMatchForUpdate() {
		String sql = "selec * from table ";
		System.err.println(SqlUtil.hasLock(sql.concat(" "), DBType.MYSQL));
		System.err.println(SqlUtil.hasLock(sql.concat(" for update"), DBType.MYSQL));
		System.err.println(SqlUtil.hasLock(sql.concat(" for update"), DBType.SQLSERVER));
		System.err.println(SqlUtil.hasLock(sql.concat(" with(rowlock xlock)"), DBType.MYSQL));
		System.err.println(SqlUtil.hasLock(sql.concat(" with(rowlock xlock)"), DBType.SQLSERVER));
		String sql1 = "select * from table with ";
		String regex = "(?i)with\\s*\\(\\s*(rowlock|xlock|updlock|holdlock)?\\,?\\s*(rowlock|xlock|updlock|holdlock)\\s*\\)";
		System.err.println(StringUtil.matches(sql1.concat("(rowlock xlock)"), regex));
		System.err.println(StringUtil.matches(sql1.concat("(rowlock,xlock)"), regex));
		System.err.println(StringUtil.matches(sql1.concat("(rowlock,updlock)"), regex));
		System.err.println(StringUtil.matches(sql1.concat("(rowlock updlock)"), regex));
		System.err.println(StringUtil.matches(sql1.concat("(holdlock updlock)"), regex));
		System.err.println(StringUtil.matches(sql1.concat("(holdlock)"), regex));
	}
	
	@Test
	public void testLike() {
		String[] ary="   a   b  c d".trim().split("\\s+");
		for(int i=0;i<ary.length;i++)
		{
			System.err.println("["+ary[i]+"]");
		}
		String sql = "支持保留字处理，对象操作自动增加保留字符号，跨数据库sql自动适配";
		
		System.err.println(StringUtil.like(sql, "保留  操作  ，跨数库".split("\\s+")));
		System.err.println(StringUtil.like(sql, "保留  操作  ， 数据库".split("\\s+")));
	
	}
}
