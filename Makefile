
libs: package-libs

package-libs:
	@gradle clean build publishToMavenLocal publish
