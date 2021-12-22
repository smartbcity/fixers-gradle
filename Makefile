test: test-mobi-rest

libs: package-libs

package-libs:
	@gradle clean build publish
