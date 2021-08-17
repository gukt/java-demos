package tech.codedog.guides;

import lombok.*;

/**
 * @Getter @Setter 可以声明在字段和类上。帮我们自动生成 setter、getter @NoArgsConstructor 添加默认构造函数
 * 添加了@Data，会自动添加默认构造函数（注意不会自动添加 RequiredArgsConstructor）、equals/canEqual、hashCode/toString
 *
 *
 * TODO：
 * @RequiredArgsConstructor onConstructor
 *
 *
 * @author https://github.com/gukt
 * @version 1.0
 */
// @Data
@NoArgsConstructor(staticName = "of")
@RequiredArgsConstructor(staticName = "of", access = AccessLevel.MODULE)
// @Data(staticConstructor="of")
public class Person {
  @Getter @Setter @NonNull String name;
}
