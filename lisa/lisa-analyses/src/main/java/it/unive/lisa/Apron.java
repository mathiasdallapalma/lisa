package it.unive.lisa;

import apron.Manager;
import apron.Abstract1;
import apron.Box;
import apron.Octagon;
import apron.Polka;
import apron.PolkaEq;
import apron.PplGrid;
import apron.PplPoly;
import apron.ApronException;
import it.unive.lisa.analysis.ScopeToken;
import it.unive.lisa.analysis.SemanticException;
import it.unive.lisa.analysis.SemanticOracle;
import it.unive.lisa.analysis.lattices.Satisfiability;
import it.unive.lisa.analysis.value.ValueDomain;
import it.unive.lisa.program.cfg.ProgramPoint;
import it.unive.lisa.symbolic.value.Identifier;
import it.unive.lisa.symbolic.value.ValueExpression;
import it.unive.lisa.util.representation.StructuredRepresentation;

import java.util.function.Predicate;


public class Apron{
	static{System.setProperty( "java.library.path", "/home/met/Documents/GitHub/lisa/lisa/lisa-analyses/src/main/java/it/unive/lisa/apron/japron" );}


public class ApronDomain {
	Manager m;
	
}
