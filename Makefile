OUTDIR=out

compile:
	rm -rf $(OUTDIR) && mkdir $(OUTDIR)
	javac -d ${OUTDIR} src/Main.java src/**/*.java

execute:
	-cd $(OUTDIR) && java Main

clean:
	-find . -type d -name $(OUTDIR) | xargs -I{} rm -rf {}
	-rm -rf $(OUTDIR)

PHONY: clean
