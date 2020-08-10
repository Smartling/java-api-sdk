package com.smartling.api.strings.v2.pto;

import com.smartling.api.v2.response.ResponseData;

import java.util.ArrayList;
import java.util.List;

public class TranslationsPTO implements ResponseData
{
    private String hashcode;
    private String stringText;
    private String parsedStringText;
    private String stringVariant;
    private String targetLocaleId;
    private String workflowStepUid;
    private List<StringKeyPTO> keys;
    private List<TranslationInternal> translations = new ArrayList<>();

    public String getHashcode()
    {
        return hashcode;
    }

    public void setHashcode(String hashcode)
    {
        this.hashcode = hashcode;
    }

    public String getStringText()
    {
        return stringText;
    }

    public void setStringText(String stringText)
    {
        this.stringText = stringText;
    }

    public String getParsedStringText()
    {
        return parsedStringText;
    }

    public void setParsedStringText(String parsedStringText)
    {
        this.parsedStringText = parsedStringText;
    }

    public String getStringVariant()
    {
        return stringVariant;
    }

    public void setStringVariant(String stringVariant)
    {
        this.stringVariant = stringVariant;
    }

    public List<StringKeyPTO> getKeys()
    {
        return keys;
    }

    public void setKeys(List<StringKeyPTO> keys)
    {
        this.keys = keys;
    }

    public String getTargetLocaleId()
    {
        return targetLocaleId;
    }

    public void setTargetLocaleId(String targetLocaleId)
    {
        this.targetLocaleId = targetLocaleId;
    }

    public String getWorkflowStepUid()
    {
        return workflowStepUid;
    }

    public void setWorkflowStepUid(String workflowStepUid)
    {
        this.workflowStepUid = workflowStepUid;
    }

    public List<TranslationInternal> getTranslations()
    {
        return translations;
    }

    public void setTranslations(List<TranslationInternal> translations)
    {
        this.translations = translations;
    }

    public static class TranslationInternal
    {
        private String pluralForm;
        private String translation;
        private String modifiedDate;

        public TranslationInternal()
        {
        }

        public TranslationInternal(String translation, String pluralForm, String modifiedDate)
        {
            this.pluralForm = pluralForm;
            this.translation = translation;
            this.modifiedDate = modifiedDate;
        }

        public String getTranslation()
        {
            return translation;
        }

        public void setTranslation(String translation)
        {
            this.translation = translation;
        }

        public String getPluralForm()
        {
            return pluralForm;
        }

        public void setPluralForm(String pluralForm)
        {
            this.pluralForm = pluralForm;
        }

        public String getModifiedDate()
        {
            return modifiedDate;
        }

        public void setModifiedDate(String modifiedDate)
        {
            this.modifiedDate = modifiedDate;
        }
    }
}
