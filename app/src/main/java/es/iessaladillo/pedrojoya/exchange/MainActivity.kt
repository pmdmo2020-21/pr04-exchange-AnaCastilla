package es.iessaladillo.pedrojoya.exchange

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import es.iessaladillo.pedrojoya.exchange.SoftInputUtils.hideSoftKeyboard
import es.iessaladillo.pedrojoya.exchange.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViews()
    }

    private fun setupViews() {
        binding.rdbFromDollar.isEnabled = false
        binding.rdbToEuro.isEnabled = false

        binding.rdgFrom.setOnCheckedChangeListener { currency, id -> checkedCurrencies(currency, id) }
        binding.rdgTo.setOnCheckedChangeListener { currency, id -> checkedCurrencies(currency, id) }

        binding.btnExchange.setOnClickListener { btnExchangeOnClick() }
        binding.edtAmount.setOnEditorActionListener { _, _, _ -> edtAmountEditorAction() }
        binding.edtAmount.setOnEditorActionListener { _, _, _ ->
            btnExchangeOnClick()
            true
        }
        binding.edtAmount.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE && !binding.edtAmount.text.endsWith(".")) {
                btnExchangeOnClick()
                hideSoftKeyboard(binding.edtAmount)
                true
            } else {
                true
            }
        }
    }


    private fun edtAmountEditorAction(): Boolean {
        btnExchangeOnClick()
        return true
    }


    private fun changeIconCurrency(imgIcon: ImageView, currency: Currency) {
        imgIcon.setImageResource(currency.drawableResId)
    }

    private fun enableCurrency(dollar: Boolean, euro: Boolean, pound: Boolean, currency: RadioGroup) {
        if (currency != binding.rdgFrom) {
            binding.rdbFromDollar.isEnabled = dollar
            binding.rdbFromEuro.isEnabled = euro
            binding.rdbFromPound.isEnabled = pound
        } else {
            binding.rdbToDollar.isEnabled = dollar
            binding.rdbToEuro.isEnabled = euro
            binding.rdbToPound.isEnabled = pound
        }
    }

    private fun checkedCurrencies(checkedCurrency: RadioGroup, checkedId: Int) {
        when(checkedId) {
            binding.rdbFromDollar.id -> {
                changeIconCurrency(binding.imgFrom, Currency.DOLLAR)
                enableCurrency(false, true, true, currency = checkedCurrency)
            }
            binding.rdbFromEuro.id -> {
                changeIconCurrency(binding.imgFrom, Currency.EURO)
                enableCurrency(true, false, true, currency = checkedCurrency)
            }
            binding.rdbFromPound.id -> {
                changeIconCurrency(binding.imgFrom, Currency.POUND)
                enableCurrency(true, true, false, currency = checkedCurrency)
            }
            binding.rdbToDollar.id -> {
                changeIconCurrency(binding.imgTo, Currency.DOLLAR)
                enableCurrency(false, true, true, currency = checkedCurrency)
            }
            binding.rdbToEuro.id -> {
                changeIconCurrency(binding.imgTo, Currency.EURO)
                enableCurrency(true, false, true, currency = checkedCurrency)
            }
            binding.rdbToPound.id -> {
                changeIconCurrency(binding.imgTo, Currency.POUND)
                enableCurrency(true, true, false, currency = checkedCurrency)
            }
        }
    }

    private fun btnExchangeOnClick() {
        var amount = binding.edtAmount.text.toString().toDouble()
        var result: Double = 0.00
        lateinit var fromCurrency: Currency
        lateinit var toCurrency: Currency

        when(binding.rdgFrom.checkedRadioButtonId) {
            binding.rdbFromDollar.id -> {
                fromCurrency = Currency.DOLLAR
                when(binding.rdgTo.checkedRadioButtonId) {
                    binding.rdbToEuro.id -> {
                        toCurrency = Currency.EURO
                        result = Currency.EURO.fromDollar(amount)
                    }
                    binding.rdbToPound.id -> {
                        toCurrency = Currency.POUND
                        result = Currency.POUND.fromDollar(amount)
                    }
                }
            }
            binding.rdbFromEuro.id -> {
                fromCurrency = Currency.EURO
                when(binding.rdgTo.checkedRadioButtonId) {
                    binding.rdbToDollar.id -> {
                        toCurrency = Currency.DOLLAR
                        result = Currency.EURO.toDollar(amount)
                    }
                    binding.rdbToPound.id -> {
                        toCurrency = Currency.POUND
                        result = Currency.EURO.toDollar(amount)
                        result = Currency.POUND.fromDollar(amount)
                    }
                }
            }
            binding.rdbFromPound.id -> {
                fromCurrency = Currency.POUND
                when(binding.rdgTo.checkedRadioButtonId) {
                    binding.rdbToDollar.id -> {
                        toCurrency = Currency.DOLLAR
                        result = Currency.POUND.toDollar(amount)
                    }
                    binding.rdbToEuro.id -> {
                        toCurrency = Currency.EURO
                        result = Currency.POUND.toDollar(amount)
                        result = Currency.EURO.fromDollar(amount)
                    }
                }
            }
        }
        hideSoftKeyboard(binding.btnExchange);
        showExchange(amount, result, fromCurrency, toCurrency)
    }

    @SuppressLint("StringFormatInvalid")
    private fun showExchange(
        currency: Double,
        result: Double,
        fromCurrency: Currency,
        toCurrency: Currency
    ) {
        Toast.makeText(
            this,
            getString(R.string.result, currency, fromCurrency.symbol, result, toCurrency.symbol),
            Toast.LENGTH_SHORT
        ).show();
    }



}

