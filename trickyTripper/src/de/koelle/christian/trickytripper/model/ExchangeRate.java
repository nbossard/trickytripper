package de.koelle.christian.trickytripper.model;

import java.io.Serializable;
import java.util.Currency;
import java.util.Date;

import de.koelle.christian.common.utils.NumberUtils;

public class ExchangeRate implements Serializable {

    private static final long serialVersionUID = 2017174474860551532L;

    private long id;
    private Currency currencyFrom;
    private Currency currencyTo;
    private Double exchangeRate;
    private String description;
    private Date updateDate;
    private ImportOrigin importOrigin;
    private boolean inversion;

    public boolean isImported() {
        return ImportOrigin.GOOGLE.equals(getImportOrigin());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public ImportOrigin getImportOrigin() {
        return importOrigin;
    }

    public void setImportOrigin(ImportOrigin importOrigin) {
        this.importOrigin = importOrigin;
    }

    public Currency getCurrencyFrom() {
        return currencyFrom;
    }

    public void setCurrencyFrom(Currency currencyFrom) {
        this.currencyFrom = currencyFrom;
    }

    public Currency getCurrencyTo() {
        return currencyTo;
    }

    public void setCurrencyTo(Currency currencyTo) {
        this.currencyTo = currencyTo;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    @Override
    public String toString() {
        return "ExchangeRate [id=" + id + ", currencyFrom=" + currencyFrom + ", currencyTo=" + currencyTo
                + ", exchangeRate=" + exchangeRate + ", description=" + description + ", updateDate=" + updateDate
                + ", importOrigin=" + importOrigin + "]";
    }

    public String getSortString() {
        return currencyFrom.getCurrencyCode() + currencyTo.getCurrencyCode();
    }

    public Double getInvertedExchangeRate() {

        return (exchangeRate == null) ? null : NumberUtils.divideForExchangeRates(Double.valueOf(1.0), exchangeRate);
    }

    public boolean isInversion() {
        return inversion;
    }

    public void setInversion(boolean inversion) {
        this.inversion = inversion;
    }

    public ExchangeRate cloneToInversion() {
        ExchangeRate result = new ExchangeRate();
        result.setCurrencyFrom(getCurrencyTo());
        result.setCurrencyTo(getCurrencyFrom());
        result.setDescription(getDescription());
        result.setExchangeRate(getInvertedExchangeRate());
        result.setId(id);
        result.setImportOrigin(getImportOrigin());
        result.setUpdateDate(getUpdateDate());
        result.setInversion(!isInversion());
        return result;
    }
}
