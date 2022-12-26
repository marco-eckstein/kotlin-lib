package com.marcoeckstein.klib.gradle.lib

@Suppress("LongLine")
data class PublicationMetadata(
    /**
     * - [POM](https://maven.apache.org/pom.html#More_Project_Information)
     * - [package.json](https://docs.npmjs.com/cli/v9/configuring-npm/package-json#name)
     * - Required by
     *   [Maven Central](https://central.sonatype.org/publish/requirements/#project-name-description-and-url)
     * . Required in
     *   [package.json](https://docs.npmjs.com/creating-a-package-json-file#required-name-and-version-fields)
     */
    val name: String,

    /**
     * - [POM](https://maven.apache.org/pom.html#More_Project_Information)
     * - [package.json](https://docs.npmjs.com/cli/v9/configuring-npm/package-json#description)
     * - Required by
     *   [Maven Central](https://central.sonatype.org/publish/requirements/#project-name-description-and-url)
     */
    val description: String,

    /**
     * - [POM](https://maven.apache.org/pom.html#More_Project_Information)
     * - ['homepage' in package.json](https://docs.npmjs.com/cli/v9/configuring-npm/package-json#homepage)
     * - Required by
     *   [Maven Central](https://central.sonatype.org/publish/requirements/#project-name-description-and-url)
     */
    val url: String,

    /**
     * - [POM](https://maven.apache.org/pom.html#More_Project_Information)
     * - Not supported in package.json
     */
    val inceptionYear: Int? = null,

    /**
     * - [POM](https://maven.apache.org/pom.html#licenses)
     * - ['license' in package.json](https://docs.npmjs.com/cli/v9/configuring-npm/package-json#license)
     *   This string value field can contain a list via `OR` separators.
     * - Required by [Maven Central](https://central.sonatype.org/publish/requirements/#license-information)
     */
    val licenses: List<License>,

    /**
     * - [POM](https://maven.apache.org/pom.html#developers)
     * - At most, a single
     *   ['author' in package.json](https://docs.npmjs.com/cli/v9/configuring-npm/package-json#people-fields-author-contributors)
     * - Required by [Maven Central](https://central.sonatype.org/publish/requirements/#developer-information)
     */
    val developers: List<Developer>,

    /**
     * - [POM](https://maven.apache.org/pom.html#issue-management)
     * - ['bugs' in package.json](https://docs.npmjs.com/cli/v9/configuring-npm/package-json#bugs)
     */
    val issueManagement: IssueManagementInfo? = null,

    /**
     * Source code management info
     *
     * - [POM](https://maven.apache.org/pom.html#scm)
     * - ['repository' in package.json](https://docs.npmjs.com/cli/v9/configuring-npm/package-json#repository)
     * - Required by [Maven Central](https://central.sonatype.org/publish/requirements/#scm-information)
     */
    val scm: SourceCodeManagementInfo,
) {

    data class License(
        /**
         * - [POM](https://maven.apache.org/pom.html#licenses)
         * - Substrings in
         *   ['license' in package.json](https://docs.npmjs.com/cli/v9/configuring-npm/package-json#license)
         * - Probably required by
         *   [Maven Central](https://central.sonatype.org/publish/requirements/#license-information)
         */
        val name: String,

        /**
         * - [POM](https://maven.apache.org/pom.html#licenses)
         * - Not supported in package.json
         * - Probably required by
         *   [Maven Central](https://central.sonatype.org/publish/requirements/#license-information)
         */
        val url: String
    )

    data class Developer(
        /**
         * - [POM](https://maven.apache.org/pom.html#developers)
         * - Not supported in package.json
         */
        val id: String?,

        /**
         * - [POM](https://maven.apache.org/pom.html#developers)
         * - [package.json](https://docs.npmjs.com/cli/v9/configuring-npm/package-json#people-fields-author-contributors)
         * - Probably required by
         *   [Maven Central](https://central.sonatype.org/publish/requirements/#developer-information)
         * - [Required in package.json](https://docs.npmjs.com/cli/v9/configuring-npm/package-json#people-fields-author-contributors)
         */
        val name: String,

        /**
         * - [POM](https://maven.apache.org/pom.html#developers)
         * - [package.json](https://docs.npmjs.com/cli/v9/configuring-npm/package-json#people-fields-author-contributors)
         * - Probably required by
         *   [Maven Central](https://central.sonatype.org/publish/requirements/#developer-information)
         */
        val email: String,

        /**
         * - [POM](https://maven.apache.org/pom.html#developers)
         * - [package.json](https://docs.npmjs.com/cli/v9/configuring-npm/package-json#people-fields-author-contributors)
         */
        val url: String?,
    )

    data class SourceCodeManagementInfo(

        /**
         * - [POM](https://maven.apache.org/pom.html#scm)
         * - Not supported in package.json
         * - Probably required by
         *   [Maven Central](https://central.sonatype.org/publish/requirements/#scm-information)
         */
        val connection: String,

        /**
         * - [POM](https://maven.apache.org/pom.html#scm)
         * - Not supported in package.json
         * - Probably required by
         *   [Maven Central](https://central.sonatype.org/publish/requirements/#scm-information)
         */
        val developerConnection: String,

        /**
         * - [POM](https://maven.apache.org/pom.html#scm)
         * - [package.json](https://docs.npmjs.com/cli/v9/configuring-npm/package-json#repository)
         * - Probably required by
         *   [Maven Central](https://central.sonatype.org/publish/requirements/#scm-information)
         */
        val url: String,
    )

    data class IssueManagementInfo(

        /**
         * - [POM](https://maven.apache.org/pom.html#issue-management)
         * - Not supported in package.json
         * - Unclear if required in POM
         */
        val system: String,

        /**
         * - [POM](https://maven.apache.org/pom.html#issue-management)
         * - [package.json](https://docs.npmjs.com/cli/v9/configuring-npm/package-json#bugs)
         * - Unclear if required in POM
         */
        val url: String,
    )
}
