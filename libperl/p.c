#include <EXTERN.h>
#include <perl.h>

void eval(char *code, char **env) {
        static PerlInterpreter *my_perl;
        STRLEN n_a;
        char *embedding[] = { "", "-e", "0" };
        PERL_SYS_INIT3(0, 0, &env);
        my_perl = perl_alloc();
        perl_construct( my_perl );
        perl_parse(my_perl, NULL, 3, embedding, NULL);
        PL_exit_flags |= PERL_EXIT_DESTRUCT_END;
        perl_run(my_perl);
        eval_pv(code, TRUE);
        perl_destruct(my_perl);
        perl_free(my_perl);
        PERL_SYS_TERM();
}

main (int argc, char **argv, char **env) {
	eval("local $\\ = \"\n\"; print for keys %::;", env);
}
