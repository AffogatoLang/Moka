@Pattern("S_VAR_ASSIGN T_EOL")
def S_STATEMENT_VAR_ASSIGN(var, eol):
    """Evaluates an S_STATEMENT(assignment) node"""
    return var + ";";
    
@Pattern("S_VAR_DEC T_EOL")
def S_STATEMENT_VAR_DEC(var, eol):
    """Evaluates an S_STATEMENT node"""
    return var + ";";
    
@Pattern("T_VAR_DEC T_IDENT")
def S_VAR_DEC(dec, ident):
    """Evaluates an S_STATEMENT node"""
    return "var " + ident;
    
@Pattern("S_VAR_DEC T_ASSIGN S_DATA")
def S_VAR_ASSIGN(vardec, assign, data):
    """Evaluates an S_STATEMENT node"""
    return vardec + "=" + data;