package org.kuali.student.brms.agenda;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;

import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.StatelessSession;
import org.drools.StatelessSessionResult;
import org.drools.compiler.PackageBuilder;
import org.drools.rule.Package;
import org.kuali.student.brms.agenda.entity.Agenda;
import org.kuali.student.brms.agenda.entity.AgendaType;
import org.kuali.student.brms.agenda.entity.Anchor;
import org.kuali.student.brms.agenda.entity.BusinessRule;
import org.kuali.student.brms.agenda.entity.BusinessRuleType;

public class AgendaDiscovery {

    /*public Agenda getAgenda( AgendaType agendaType, 
                             BusinessRuleType businessRuleType, 
                             Anchor anchor ) {
        // This is for demo purposes only
        // A decision table rule - should be using a rules engine to do this
        if ( agendaType.getType().equals( "Student Enrolment" ) &&
             businessRuleType.getType().equals( "Pre-Requisite" ) &&
             anchor.getAnchorType().equals( "CLU.course" ) ) {
            
            BusinessRule rule = new BusinessRule("uuid-123-456-789", businessRuleType);
            rule.setAnchor( anchor );
            Agenda agenda = new Agenda( "Student Enrolment Agenda", agendaType );
            agenda.addBusinessRule( rule );
            return agenda;
        }
        return null;
    }

    public Agenda getAgenda2( AgendaType agendaType, 
                              Anchor anchor ) {
        // This is for demo purposes only
        // A decision table rule - should be using a rules engine to do this
        if ( agendaType.getType().equals( "Student Enrolment" ) &&
             agendaType.getBusinessRuleTypes().contains( "Pre-Requisite" ) &&
             anchor.getAnchorType().equals( "CLU.course" ) ) {
            
            BusinessRuleType ruleType = getPreReqType( agendaType.getBusinessRuleTypes() );
            BusinessRule rule = new BusinessRule("uuid-123-456-789", ruleType);
            rule.setAnchor( anchor );
            Agenda agenda = new Agenda( "Student Enrolment Agenda", agendaType );
            agenda.addBusinessRule( rule );
            return agenda;
        }
        return null;
    }
    
    private BusinessRuleType getPreReqType( Collection<BusinessRuleType> col ) {
        for( BusinessRuleType type : col ) {
            if ( type.equals( "Pre-Requisite" ) ) {
                return type;
            }
        }
        return null;
    }*/

    public Agenda getAgenda( AgendaRequest request, Anchor anchor ) {
        InputStream is = AgendaDiscovery.class.getResourceAsStream("/agenda.drl");
        StatelessSessionResult result = null;
        
        try {
            RuleBase ruleBase = loadRuleBase( new InputStreamReader( is ) );
            
            // Get the agenda type
            result = executeRule( ruleBase, request );
        } catch( Exception e ) {
            throw new RuntimeException( "Loading rule base or executing rules failed", e );
        }

        // Retrieve agenda type from rule set
        Iterator it = result.iterateObjects();
        AgendaType agendaType = null;
        while( it != null && it.hasNext() ) {
            Object obj = it.next();
            //System.out.println( obj.getClass() + " = " + obj );
            if ( obj instanceof AgendaType ) {
                agendaType = (AgendaType) obj;
                break;
            }
        }
        // Create actual agenda with actual rules
        Agenda agenda = new Agenda( agendaType.getName(), agendaType );
        retrieveRulesFromDatabase( agenda );

        return agenda;
    }
    
    private void retrieveRulesFromDatabase( Agenda agenda ) {
        for( BusinessRuleType type : agenda.getAgendaType().getBusinessRuleTypes() ) {
            agenda.addBusinessRule( new BusinessRule( ""+type.hashCode(), type ) );
        }
    }
    
    private RuleBase loadRuleBase( Reader drl ) throws Exception
    {
        Thread currentThread = Thread.currentThread();
        ClassLoader oldClassLoader = currentThread.getContextClassLoader();
        ClassLoader newClassLoader = AgendaDiscovery.class.getClassLoader();

        try
        {
            currentThread.setContextClassLoader( newClassLoader );
        
            PackageBuilder builder = new PackageBuilder();
            builder.addPackageFromDrl(drl);
            //Get the compiled package (which is serializable)
            Package pkg = builder.getPackage();
            //Add the package to a rulebase (deploy the rule package).
            RuleBase ruleBase = RuleBaseFactory.newRuleBase();
            ruleBase.addPackage(pkg);
      
            return ruleBase;
        }
        finally
        {
            currentThread.setContextClassLoader( oldClassLoader );
        }
    }

    private StatelessSessionResult executeRule( RuleBase ruleBase, Object fact )
        throws Exception
    {
        StatelessSession session = ruleBase.newStatelessSession();
        return session.executeWithResults( fact );
    }
}
