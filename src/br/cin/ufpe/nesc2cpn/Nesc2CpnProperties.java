package br.cin.ufpe.nesc2cpn;

import java.net.URL;

/**
 *
 * @author avld
 */
public class Nesc2CpnProperties
{
    private boolean keep;
    private boolean onlyCreateModel;
    private boolean createApplicationModel;
    private boolean reduction;
    private String outputDir;
    private String projectDir;
    private String modelName;
    private String functionName;
    private URL evaluateServiceUrl;
    
    public Nesc2CpnProperties()
    {
        //----------------------- Properties
        //keep          salva o modelo e o resultado
        //onlycreate    apenas cria o model (não avalia)
        //function      informa qual função deve ser modelada (as outras serão ignoradas)
        //output        informa a pasta onde deve salva os arquivos (modelo e resultado)
        
        keep = false;
        onlyCreateModel = false;
        createApplicationModel = true;
        outputDir = "./";
        modelName = "model.cpn";
        evaluateServiceUrl = null;
    }

    public boolean isKeep() {
        return keep;
    }

    public void setKeep(boolean keep) {
        this.keep = keep;
    }

    public boolean isOnlyCreateModel() {
        return onlyCreateModel;
    }

    public void setOnlyCreateModel(boolean onlyCreateModel) {
        this.onlyCreateModel = onlyCreateModel;
    }

    public boolean isCreateApplicationModel() {
        return createApplicationModel;
    }

    public void setCreateApplicationModel(boolean isApplication)
    {
        this.createApplicationModel = isApplication;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName)
    {
        this.functionName = functionName;
        this.setCreateApplicationModel( functionName != null );
    }

    public URL getEvaluateServiceUrl() {
        return evaluateServiceUrl;
    }

    public void setEvaluateServiceUrl(URL evaluateServiceUrl) {
        this.evaluateServiceUrl = evaluateServiceUrl;
    }

    public String getProjectDir() {
        return projectDir;
    }

    public void setProjectDir(String projectDir) {
        this.projectDir = projectDir;
    }

    public boolean isReduction() {
        return reduction;
    }

    public void setReduction(boolean reduction) {
        this.reduction = reduction;
    }
    
}
