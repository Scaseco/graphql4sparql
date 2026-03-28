BASE = apache-jena-fuseki-6.0.0

# ----------------------------------------------------------------------
# Variables
# ----------------------------------------------------------------------
CWD            := $(shell pwd)
PARENT_DIR     := $(CWD)/..
FUSEKI_BASE    := target/$(BASE)

# The jar produced by the parent build
PLUGIN_JAR_PATTERN := $(PLUGIN_SRC_DIR)/target/*-jar-with-dependencies.jar

# Where the jar should be installed inside Fuseki
FUSEKI_JAR_INSTALLED_PATTERN := $(FUSEKI_BASE)/run/extra/%-jar-with-dependencies.jar

# The jar that the parent build creates (wildcard works even if the version changes)
PLUGIN_SRC_DIR := $(PARENT_DIR)/graphql4sparql-pkg-parent/graphql4sparql-pkg-fuseki-mod

# Source: https://stackoverflow.com/questions/4219255/how-do-you-get-the-list-of-targets-in-a-makefile
.PHONY: help build-fuseki-mod clean-fuseki-mod setup-fuseki copy-example-config copy-fuseki-mod

.ONESHELL:
help:   ## Show these help instructions
	@sed -rn 's/^([a-zA-Z_-]+):.*?## (.*)$$/"\1" "\2"/p' < $(MAKEFILE_LIST) | xargs printf "make %-20s# %s\n"

target:
	mkdir -p target

setup-fuseki: target ## Setup Fuseki with the GraphQl4Sparql Fuseki Mod and example data
	curl -C - -L -O "https://dlcdn.apache.org/jena/binaries/$(BASE).tar.gz" --output-dir target
	tar xf "target/$(BASE).tar.gz"

copy-example-config: setup-fuseki
	mkdir -p "$(FUSEKI_BASE)/run/configuration"
	# --update=none -> Do not overwrite existing files
	cp --update=none config.ttl $(FUSEKI_BASE)/run
	cp --update=none graphql-test_ds.ttl mona-lisa.ttl mona-lisa.graphql "$(FUSEKI_BASE)/run/configuration"
	mkdir -p "$(FUSEKI_BASE)/run/content"
	cp --update=none mona-lisa.ttl "$(FUSEKI_BASE)/run/content"

copy-fuseki-mod: $(FUSEKI_BASE)/run/extra/graphql4sparql-fuseki-mod.jar

setup-example: copy-example-config copy-fuseki-mod
	PORT=3030
	echo "Fuseki set up! To start: "
	echo "cd $(FUSEKI_BASE)"
	echo "./fuseki-server --config=run/config.ttl --port $${PORT}"
	echo "The Web UI will be available at http://localhost:$${PORT}/"

$(FUSEKI_BASE)/run/extra/graphql4sparql-fuseki-mod.jar: setup-fuseki
	@$(MAKE) -C $(PARENT_DIR) uberjar-fuseki-mod
	file=`find '$(PARENT_DIR)/graphql4sparql-pkg-parent/graphql4sparql-pkg-fuseki-mod/target' -name '*-jar-with-dependencies.jar'`
	cp "$$file" "$(FUSEKI_BASE)/run/extra/graphql4sparql-fuseki-mod.jar"

build-fuseki-mod: $(FUSEKI_BASE)/run/extra/graphql4sparql-fuseki-mod.jar

clean-fuseki-mod:
	rm -f $(FUSEKI_BASE)/run/extra/graphql4sparql-fuseki-mod.jar

