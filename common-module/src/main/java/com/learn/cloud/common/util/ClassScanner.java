package com.learn.cloud.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author zhang
 * @projectName spring-cloud-alibaba
 * @title ClassScaner
 * @package com.learn.cloud.common.util
 * @description 获取扫描包下的clazz
 * @date 2020/1/3 11:04 上午
 */
@Slf4j
@Component
public class ClassScanner implements ResourceLoaderAware {
	
	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();;
	
	private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);
	;
	
	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourcePatternResolver = ResourcePatternUtils
				.getResourcePatternResolver(resourceLoader);
		this.metadataReaderFactory = new CachingMetadataReaderFactory(
				resourceLoader);
	}
	
	/**
	 * 获取扫描路径下的class字节对象集合 <BR>
	 * @param basePackage 扫描路径
	 * @return java.util.Set<java.lang.Class>
	 * @author zhangwei
	 * @createTime 2020/1/3 11:27 上午
	 */
	public Set<Class> doScan(String basePackage) {
		
		Set<Class> classes = new HashSet<>();
		String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
				+ ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage))
				+ "/**/*.class";
		log.info("资源路径：{}",packageSearchPath);
		try {
			// 获取路径下的资源
			Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
			for (int i = 0; i < resources.length ; i++) {
				
				Resource resource = resources[i];
				// 可读
				if (resource.isReadable()) {
					MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
					Class<?> aClass = Class.forName(metadataReader.getClassMetadata().getClassName());
					classes.add(aClass);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return  classes;
	}
	
	
	public static Set<Class> scan(List<String> basePackages) {
		ClassScanner cs = new ClassScanner();
		
		Set<Class> classes = new HashSet<>();
		for (String s : basePackages) {
			classes.addAll(cs.doScan(s));
		}
		return classes;
	}
	
	
	
}
