package com.tymdoc.view.template;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.ConfigurationException;
import com.tymdoc.config.EasyJavadocConfigComponent;
import com.tymdoc.model.EasyJavadocConfiguration;
import com.tymdoc.model.EasyJavadocConfiguration.TemplateConfig;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nls.Capitalization;

import java.util.Objects;
import java.util.TreeMap;

/**
 * @author <a href="mailto:wangchao.star@gmail.com">wangchao</a>
 * @version 1.0.0
 * @since 2019-11-10 17:35:00
 */
public class MethodConfigurable extends AbstractTemplateConfigurable {
    private EasyJavadocConfiguration config = ServiceManager.getService(EasyJavadocConfigComponent.class).getState();
    private MethodConfigView view = new MethodConfigView(config);

    @Nls(capitalization = Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "方法注释模板配置";
    }

    @Override
    public AbstractTemplateConfigView getView() {
        return view;
    }

    @Override
    public boolean isModified() {
        TemplateConfig templateConfig = config.getMethodTemplateConfig();
        if (!Objects.equals(templateConfig.getIsDefault(), view.isDefault())) {
            return true;
        }
        if (!Objects.equals(templateConfig.getTemplate(), view.getTemplate())) {
            return true;
        }
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {
        TemplateConfig templateConfig = config.getMethodTemplateConfig();
        templateConfig.setIsDefault(view.isDefault());
        templateConfig.setTemplate(view.getTemplate());
        if (templateConfig.getCustomMap() == null) {
            templateConfig.setCustomMap(new TreeMap<>());
        }
        if (!view.isDefault()) {
            if (StringUtils.isBlank(view.getTemplate())) {
                throw new ConfigurationException("使用自定义模板，模板不能为空");
            }
            String temp = StringUtils.strip(view.getTemplate());
            if (!temp.startsWith("/**") || !temp.endsWith("*/")) {
                throw new ConfigurationException("模板格式不正确，正确的javadoc应该以\"/**\"开头，以\"*/\"结束");
            }
        }
    }

    @Override
    public void reset() {
        TemplateConfig templateConfig = config.getMethodTemplateConfig();
        if (BooleanUtils.isTrue(templateConfig.getIsDefault())) {
            view.setDefault(true);
        } else {
            view.setDefault(false);
        }
        view.setTemplate(templateConfig.getTemplate());
    }
}
