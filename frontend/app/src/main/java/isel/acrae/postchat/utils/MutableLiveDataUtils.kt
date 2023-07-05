package isel.acrae.postchat.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData


/**
 * Observes mutable live data until its value is true, than executes onDone
 */
fun <T> MutableLiveData<Boolean>.done(owner: LifecycleOwner, onDone : () -> T) {
    this.observe(owner) {
        if(it) onDone()
    }
}
