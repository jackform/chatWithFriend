// INetworkService.aidl
package org.jackform.innocent;
import org.jackform.innocent.IResult;
// Declare any non-default types here with import statements

interface INetworkService {
     void init(IResult result,in String hash);
     void connect();
     void executeRequest(int requestID,in Bundle request);
     void close();
}
