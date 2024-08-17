package site.hansi.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文档地址
 *
 * @author 北京智匠坊
 */
@Getter
@AllArgsConstructor
public enum DocumentEnum {

    REDIS_INSTALL("https://www.hansi.site/", "Redis 安装文档"),
    TENANT("https://www.hansi.site/", "SaaS 多租户文档");

    private final String url;
    private final String memo;

}
