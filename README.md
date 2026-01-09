# http-client-examples-java

Backend-to-backend integration with external services is different from building a frontend client. 
Once you are past the initial API exploration phase, you should build true ownership of the integration.  
This includes awareness of the load, operational conditions, understanding the role the integration plays in the business.
Not to forget robustness and security. Also, the integration design should enable team autonomy and agile decision making.

This repository contains supportive examples to the guidance on how to own a backend-to-backend integration.

## Getting started

* Make sure you have JDK installed
* Use Bazel using Bazelisk: https://bazel.build/install/bazelisk
* Start the service: 

```shell
bazel run //travelvac-service
```

* Explore and run a client. For example, the basic one:

```shell
bazel run //travelvac-client-basic
```