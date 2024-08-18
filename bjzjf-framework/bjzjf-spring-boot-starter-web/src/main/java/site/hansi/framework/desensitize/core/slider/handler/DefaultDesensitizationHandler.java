package site.hansi.framework.desensitize.core.slider.handler;

import site.hansi.framework.desensitize.core.slider.annotation.SliderDesensitize;

/**
 * {@link SliderDesensitize} 的脱敏处理器
 *
 * @author 北京智匠坊
 */
public class DefaultDesensitizationHandler extends AbstractSliderDesensitizationHandler<SliderDesensitize> {

    @Override
    Integer getPrefixKeep(SliderDesensitize annotation) {
        return annotation.prefixKeep();
    }

    @Override
    Integer getSuffixKeep(SliderDesensitize annotation) {
        return annotation.suffixKeep();
    }

    @Override
    String getReplacer(SliderDesensitize annotation) {
        return annotation.replacer();
    }

}
