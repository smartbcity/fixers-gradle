
libs: package-libs
docs:
	echo 'No Docs'

package-libs:
	@gradle clean build publishToMavenLocal publish
