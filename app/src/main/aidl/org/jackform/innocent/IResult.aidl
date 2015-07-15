// IResult.aidl
package org.jackform.innocent;

interface IResult {
    void onResult(int responseID, in Bundle result);
}
