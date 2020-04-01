package com.mysql.cj.x.protobuf;

import com.google.protobuf.AbstractMessage;
import com.google.protobuf.AbstractMessageLite;
import com.google.protobuf.AbstractParser;
import com.google.protobuf.ByteString;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.Descriptors;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Internal;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.Parser;
import com.google.protobuf.UnknownFieldSet;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public final class MysqlxSession {
  public static void registerAllExtensions(ExtensionRegistryLite registry) {}
  
  public static void registerAllExtensions(ExtensionRegistry registry) {
    registerAllExtensions((ExtensionRegistryLite)registry);
  }
  
  public static final class AuthenticateStart extends GeneratedMessageV3 implements AuthenticateStartOrBuilder {
    private static final long serialVersionUID = 0L;
    
    private int bitField0_;
    
    public static final int MECH_NAME_FIELD_NUMBER = 1;
    
    private volatile Object mechName_;
    
    public static final int AUTH_DATA_FIELD_NUMBER = 2;
    
    private ByteString authData_;
    
    public static final int INITIAL_RESPONSE_FIELD_NUMBER = 3;
    
    private ByteString initialResponse_;
    
    private byte memoizedIsInitialized;
    
    private AuthenticateStart(GeneratedMessageV3.Builder<?> builder) {
      super(builder);
      this.memoizedIsInitialized = -1;
    }
    
    private AuthenticateStart() {
      this.memoizedIsInitialized = -1;
      this.mechName_ = "";
      this.authData_ = ByteString.EMPTY;
      this.initialResponse_ = ByteString.EMPTY;
    }
    
    public final UnknownFieldSet getUnknownFields() {
      return this.unknownFields;
    }
    
    private AuthenticateStart(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null)
        throw new NullPointerException(); 
      int mutable_bitField0_ = 0;
      UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          ByteString bs;
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              continue;
            case 10:
              bs = input.readBytes();
              this.bitField0_ |= 0x1;
              this.mechName_ = bs;
              continue;
            case 18:
              this.bitField0_ |= 0x2;
              this.authData_ = input.readBytes();
              continue;
            case 26:
              this.bitField0_ |= 0x4;
              this.initialResponse_ = input.readBytes();
              continue;
          } 
          if (!parseUnknownField(input, unknownFields, extensionRegistry, tag))
            done = true; 
        } 
      } catch (InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (IOException e) {
        throw (new InvalidProtocolBufferException(e)).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      } 
    }
    
    public static final Descriptors.Descriptor getDescriptor() {
      return MysqlxSession.internal_static_Mysqlx_Session_AuthenticateStart_descriptor;
    }
    
    protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
      return MysqlxSession.internal_static_Mysqlx_Session_AuthenticateStart_fieldAccessorTable.ensureFieldAccessorsInitialized(AuthenticateStart.class, Builder.class);
    }
    
    public boolean hasMechName() {
      return ((this.bitField0_ & 0x1) == 1);
    }
    
    public String getMechName() {
      Object ref = this.mechName_;
      if (ref instanceof String)
        return (String)ref; 
      ByteString bs = (ByteString)ref;
      String s = bs.toStringUtf8();
      if (bs.isValidUtf8())
        this.mechName_ = s; 
      return s;
    }
    
    public ByteString getMechNameBytes() {
      Object ref = this.mechName_;
      if (ref instanceof String) {
        ByteString b = ByteString.copyFromUtf8((String)ref);
        this.mechName_ = b;
        return b;
      } 
      return (ByteString)ref;
    }
    
    public boolean hasAuthData() {
      return ((this.bitField0_ & 0x2) == 2);
    }
    
    public ByteString getAuthData() {
      return this.authData_;
    }
    
    public boolean hasInitialResponse() {
      return ((this.bitField0_ & 0x4) == 4);
    }
    
    public ByteString getInitialResponse() {
      return this.initialResponse_;
    }
    
    public final boolean isInitialized() {
      byte isInitialized = this.memoizedIsInitialized;
      if (isInitialized == 1)
        return true; 
      if (isInitialized == 0)
        return false; 
      if (!hasMechName()) {
        this.memoizedIsInitialized = 0;
        return false;
      } 
      this.memoizedIsInitialized = 1;
      return true;
    }
    
    public void writeTo(CodedOutputStream output) throws IOException {
      if ((this.bitField0_ & 0x1) == 1)
        GeneratedMessageV3.writeString(output, 1, this.mechName_); 
      if ((this.bitField0_ & 0x2) == 2)
        output.writeBytes(2, this.authData_); 
      if ((this.bitField0_ & 0x4) == 4)
        output.writeBytes(3, this.initialResponse_); 
      this.unknownFields.writeTo(output);
    }
    
    public int getSerializedSize() {
      int size = this.memoizedSize;
      if (size != -1)
        return size; 
      size = 0;
      if ((this.bitField0_ & 0x1) == 1)
        size += GeneratedMessageV3.computeStringSize(1, this.mechName_); 
      if ((this.bitField0_ & 0x2) == 2)
        size += 
          CodedOutputStream.computeBytesSize(2, this.authData_); 
      if ((this.bitField0_ & 0x4) == 4)
        size += 
          CodedOutputStream.computeBytesSize(3, this.initialResponse_); 
      size += this.unknownFields.getSerializedSize();
      this.memoizedSize = size;
      return size;
    }
    
    public boolean equals(Object obj) {
      if (obj == this)
        return true; 
      if (!(obj instanceof AuthenticateStart))
        return super.equals(obj); 
      AuthenticateStart other = (AuthenticateStart)obj;
      boolean result = true;
      result = (result && hasMechName() == other.hasMechName());
      if (hasMechName())
        result = (result && getMechName().equals(other.getMechName())); 
      result = (result && hasAuthData() == other.hasAuthData());
      if (hasAuthData())
        result = (result && getAuthData().equals(other.getAuthData())); 
      result = (result && hasInitialResponse() == other.hasInitialResponse());
      if (hasInitialResponse())
        result = (result && getInitialResponse().equals(other.getInitialResponse())); 
      result = (result && this.unknownFields.equals(other.unknownFields));
      return result;
    }
    
    public int hashCode() {
      if (this.memoizedHashCode != 0)
        return this.memoizedHashCode; 
      int hash = 41;
      hash = 19 * hash + getDescriptor().hashCode();
      if (hasMechName()) {
        hash = 37 * hash + 1;
        hash = 53 * hash + getMechName().hashCode();
      } 
      if (hasAuthData()) {
        hash = 37 * hash + 2;
        hash = 53 * hash + getAuthData().hashCode();
      } 
      if (hasInitialResponse()) {
        hash = 37 * hash + 3;
        hash = 53 * hash + getInitialResponse().hashCode();
      } 
      hash = 29 * hash + this.unknownFields.hashCode();
      this.memoizedHashCode = hash;
      return hash;
    }
    
    public static AuthenticateStart parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
      return (AuthenticateStart)PARSER.parseFrom(data);
    }
    
    public static AuthenticateStart parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (AuthenticateStart)PARSER.parseFrom(data, extensionRegistry);
    }
    
    public static AuthenticateStart parseFrom(ByteString data) throws InvalidProtocolBufferException {
      return (AuthenticateStart)PARSER.parseFrom(data);
    }
    
    public static AuthenticateStart parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (AuthenticateStart)PARSER.parseFrom(data, extensionRegistry);
    }
    
    public static AuthenticateStart parseFrom(byte[] data) throws InvalidProtocolBufferException {
      return (AuthenticateStart)PARSER.parseFrom(data);
    }
    
    public static AuthenticateStart parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (AuthenticateStart)PARSER.parseFrom(data, extensionRegistry);
    }
    
    public static AuthenticateStart parseFrom(InputStream input) throws IOException {
      return 
        (AuthenticateStart)GeneratedMessageV3.parseWithIOException(PARSER, input);
    }
    
    public static AuthenticateStart parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return 
        (AuthenticateStart)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
    }
    
    public static AuthenticateStart parseDelimitedFrom(InputStream input) throws IOException {
      return 
        (AuthenticateStart)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
    }
    
    public static AuthenticateStart parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return 
        (AuthenticateStart)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    
    public static AuthenticateStart parseFrom(CodedInputStream input) throws IOException {
      return 
        (AuthenticateStart)GeneratedMessageV3.parseWithIOException(PARSER, input);
    }
    
    public static AuthenticateStart parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return 
        (AuthenticateStart)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
    }
    
    public Builder newBuilderForType() {
      return newBuilder();
    }
    
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    
    public static Builder newBuilder(AuthenticateStart prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    
    public Builder toBuilder() {
      return (this == DEFAULT_INSTANCE) ? new Builder() : (new Builder())
        .mergeFrom(this);
    }
    
    protected Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    
    public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements MysqlxSession.AuthenticateStartOrBuilder {
      private int bitField0_;
      
      private Object mechName_;
      
      private ByteString authData_;
      
      private ByteString initialResponse_;
      
      public static final Descriptors.Descriptor getDescriptor() {
        return MysqlxSession.internal_static_Mysqlx_Session_AuthenticateStart_descriptor;
      }
      
      protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
        return MysqlxSession.internal_static_Mysqlx_Session_AuthenticateStart_fieldAccessorTable
          .ensureFieldAccessorsInitialized(MysqlxSession.AuthenticateStart.class, Builder.class);
      }
      
      private Builder() {
        this.mechName_ = "";
        this.authData_ = ByteString.EMPTY;
        this.initialResponse_ = ByteString.EMPTY;
        maybeForceBuilderInitialization();
      }
      
      private Builder(GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        this.mechName_ = "";
        this.authData_ = ByteString.EMPTY;
        this.initialResponse_ = ByteString.EMPTY;
        maybeForceBuilderInitialization();
      }
      
      private void maybeForceBuilderInitialization() {
        if (MysqlxSession.AuthenticateStart.alwaysUseFieldBuilders);
      }
      
      public Builder clear() {
        super.clear();
        this.mechName_ = "";
        this.bitField0_ &= 0xFFFFFFFE;
        this.authData_ = ByteString.EMPTY;
        this.bitField0_ &= 0xFFFFFFFD;
        this.initialResponse_ = ByteString.EMPTY;
        this.bitField0_ &= 0xFFFFFFFB;
        return this;
      }
      
      public Descriptors.Descriptor getDescriptorForType() {
        return MysqlxSession.internal_static_Mysqlx_Session_AuthenticateStart_descriptor;
      }
      
      public MysqlxSession.AuthenticateStart getDefaultInstanceForType() {
        return MysqlxSession.AuthenticateStart.getDefaultInstance();
      }
      
      public MysqlxSession.AuthenticateStart build() {
        MysqlxSession.AuthenticateStart result = buildPartial();
        if (!result.isInitialized())
          throw newUninitializedMessageException(result); 
        return result;
      }
      
      public MysqlxSession.AuthenticateStart buildPartial() {
        MysqlxSession.AuthenticateStart result = new MysqlxSession.AuthenticateStart(this);
        int from_bitField0_ = this.bitField0_;
        int to_bitField0_ = 0;
        if ((from_bitField0_ & 0x1) == 1)
          to_bitField0_ |= 0x1; 
        result.mechName_ = this.mechName_;
        if ((from_bitField0_ & 0x2) == 2)
          to_bitField0_ |= 0x2; 
        result.authData_ = this.authData_;
        if ((from_bitField0_ & 0x4) == 4)
          to_bitField0_ |= 0x4; 
        result.initialResponse_ = this.initialResponse_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder clone() {
        return (Builder)super.clone();
      }
      
      public Builder setField(Descriptors.FieldDescriptor field, Object value) {
        return (Builder)super.setField(field, value);
      }
      
      public Builder clearField(Descriptors.FieldDescriptor field) {
        return (Builder)super.clearField(field);
      }
      
      public Builder clearOneof(Descriptors.OneofDescriptor oneof) {
        return (Builder)super.clearOneof(oneof);
      }
      
      public Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
        return (Builder)super.setRepeatedField(field, index, value);
      }
      
      public Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
        return (Builder)super.addRepeatedField(field, value);
      }
      
      public Builder mergeFrom(Message other) {
        if (other instanceof MysqlxSession.AuthenticateStart)
          return mergeFrom((MysqlxSession.AuthenticateStart)other); 
        super.mergeFrom(other);
        return this;
      }
      
      public Builder mergeFrom(MysqlxSession.AuthenticateStart other) {
        if (other == MysqlxSession.AuthenticateStart.getDefaultInstance())
          return this; 
        if (other.hasMechName()) {
          this.bitField0_ |= 0x1;
          this.mechName_ = other.mechName_;
          onChanged();
        } 
        if (other.hasAuthData())
          setAuthData(other.getAuthData()); 
        if (other.hasInitialResponse())
          setInitialResponse(other.getInitialResponse()); 
        mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasMechName())
          return false; 
        return true;
      }
      
      public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        MysqlxSession.AuthenticateStart parsedMessage = null;
        try {
          parsedMessage = (MysqlxSession.AuthenticateStart)MysqlxSession.AuthenticateStart.PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (InvalidProtocolBufferException e) {
          parsedMessage = (MysqlxSession.AuthenticateStart)e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null)
            mergeFrom(parsedMessage); 
        } 
        return this;
      }
      
      public boolean hasMechName() {
        return ((this.bitField0_ & 0x1) == 1);
      }
      
      public String getMechName() {
        Object ref = this.mechName_;
        if (!(ref instanceof String)) {
          ByteString bs = (ByteString)ref;
          String s = bs.toStringUtf8();
          if (bs.isValidUtf8())
            this.mechName_ = s; 
          return s;
        } 
        return (String)ref;
      }
      
      public ByteString getMechNameBytes() {
        Object ref = this.mechName_;
        if (ref instanceof String) {
          ByteString b = ByteString.copyFromUtf8((String)ref);
          this.mechName_ = b;
          return b;
        } 
        return (ByteString)ref;
      }
      
      public Builder setMechName(String value) {
        if (value == null)
          throw new NullPointerException(); 
        this.bitField0_ |= 0x1;
        this.mechName_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearMechName() {
        this.bitField0_ &= 0xFFFFFFFE;
        this.mechName_ = MysqlxSession.AuthenticateStart.getDefaultInstance().getMechName();
        onChanged();
        return this;
      }
      
      public Builder setMechNameBytes(ByteString value) {
        if (value == null)
          throw new NullPointerException(); 
        this.bitField0_ |= 0x1;
        this.mechName_ = value;
        onChanged();
        return this;
      }
      
      public boolean hasAuthData() {
        return ((this.bitField0_ & 0x2) == 2);
      }
      
      public ByteString getAuthData() {
        return this.authData_;
      }
      
      public Builder setAuthData(ByteString value) {
        if (value == null)
          throw new NullPointerException(); 
        this.bitField0_ |= 0x2;
        this.authData_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearAuthData() {
        this.bitField0_ &= 0xFFFFFFFD;
        this.authData_ = MysqlxSession.AuthenticateStart.getDefaultInstance().getAuthData();
        onChanged();
        return this;
      }
      
      public boolean hasInitialResponse() {
        return ((this.bitField0_ & 0x4) == 4);
      }
      
      public ByteString getInitialResponse() {
        return this.initialResponse_;
      }
      
      public Builder setInitialResponse(ByteString value) {
        if (value == null)
          throw new NullPointerException(); 
        this.bitField0_ |= 0x4;
        this.initialResponse_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearInitialResponse() {
        this.bitField0_ &= 0xFFFFFFFB;
        this.initialResponse_ = MysqlxSession.AuthenticateStart.getDefaultInstance().getInitialResponse();
        onChanged();
        return this;
      }
      
      public final Builder setUnknownFields(UnknownFieldSet unknownFields) {
        return (Builder)super.setUnknownFields(unknownFields);
      }
      
      public final Builder mergeUnknownFields(UnknownFieldSet unknownFields) {
        return (Builder)super.mergeUnknownFields(unknownFields);
      }
    }
    
    private static final AuthenticateStart DEFAULT_INSTANCE = new AuthenticateStart();
    
    public static AuthenticateStart getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }
    
    @Deprecated
    public static final Parser<AuthenticateStart> PARSER = (Parser<AuthenticateStart>)new AbstractParser<AuthenticateStart>() {
        public MysqlxSession.AuthenticateStart parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
          return new MysqlxSession.AuthenticateStart(input, extensionRegistry);
        }
      };
    
    public static Parser<AuthenticateStart> parser() {
      return PARSER;
    }
    
    public Parser<AuthenticateStart> getParserForType() {
      return PARSER;
    }
    
    public AuthenticateStart getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }
  }
  
  public static final class AuthenticateContinue extends GeneratedMessageV3 implements AuthenticateContinueOrBuilder {
    private static final long serialVersionUID = 0L;
    
    private int bitField0_;
    
    public static final int AUTH_DATA_FIELD_NUMBER = 1;
    
    private ByteString authData_;
    
    private byte memoizedIsInitialized;
    
    private AuthenticateContinue(GeneratedMessageV3.Builder<?> builder) {
      super(builder);
      this.memoizedIsInitialized = -1;
    }
    
    private AuthenticateContinue() {
      this.memoizedIsInitialized = -1;
      this.authData_ = ByteString.EMPTY;
    }
    
    public final UnknownFieldSet getUnknownFields() {
      return this.unknownFields;
    }
    
    private AuthenticateContinue(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null)
        throw new NullPointerException(); 
      int mutable_bitField0_ = 0;
      UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              continue;
            case 10:
              this.bitField0_ |= 0x1;
              this.authData_ = input.readBytes();
              continue;
          } 
          if (!parseUnknownField(input, unknownFields, extensionRegistry, tag))
            done = true; 
        } 
      } catch (InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (IOException e) {
        throw (new InvalidProtocolBufferException(e)).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      } 
    }
    
    public static final Descriptors.Descriptor getDescriptor() {
      return MysqlxSession.internal_static_Mysqlx_Session_AuthenticateContinue_descriptor;
    }
    
    protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
      return MysqlxSession.internal_static_Mysqlx_Session_AuthenticateContinue_fieldAccessorTable.ensureFieldAccessorsInitialized(AuthenticateContinue.class, Builder.class);
    }
    
    public boolean hasAuthData() {
      return ((this.bitField0_ & 0x1) == 1);
    }
    
    public ByteString getAuthData() {
      return this.authData_;
    }
    
    public final boolean isInitialized() {
      byte isInitialized = this.memoizedIsInitialized;
      if (isInitialized == 1)
        return true; 
      if (isInitialized == 0)
        return false; 
      if (!hasAuthData()) {
        this.memoizedIsInitialized = 0;
        return false;
      } 
      this.memoizedIsInitialized = 1;
      return true;
    }
    
    public void writeTo(CodedOutputStream output) throws IOException {
      if ((this.bitField0_ & 0x1) == 1)
        output.writeBytes(1, this.authData_); 
      this.unknownFields.writeTo(output);
    }
    
    public int getSerializedSize() {
      int size = this.memoizedSize;
      if (size != -1)
        return size; 
      size = 0;
      if ((this.bitField0_ & 0x1) == 1)
        size += 
          CodedOutputStream.computeBytesSize(1, this.authData_); 
      size += this.unknownFields.getSerializedSize();
      this.memoizedSize = size;
      return size;
    }
    
    public boolean equals(Object obj) {
      if (obj == this)
        return true; 
      if (!(obj instanceof AuthenticateContinue))
        return super.equals(obj); 
      AuthenticateContinue other = (AuthenticateContinue)obj;
      boolean result = true;
      result = (result && hasAuthData() == other.hasAuthData());
      if (hasAuthData())
        result = (result && getAuthData().equals(other.getAuthData())); 
      result = (result && this.unknownFields.equals(other.unknownFields));
      return result;
    }
    
    public int hashCode() {
      if (this.memoizedHashCode != 0)
        return this.memoizedHashCode; 
      int hash = 41;
      hash = 19 * hash + getDescriptor().hashCode();
      if (hasAuthData()) {
        hash = 37 * hash + 1;
        hash = 53 * hash + getAuthData().hashCode();
      } 
      hash = 29 * hash + this.unknownFields.hashCode();
      this.memoizedHashCode = hash;
      return hash;
    }
    
    public static AuthenticateContinue parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
      return (AuthenticateContinue)PARSER.parseFrom(data);
    }
    
    public static AuthenticateContinue parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (AuthenticateContinue)PARSER.parseFrom(data, extensionRegistry);
    }
    
    public static AuthenticateContinue parseFrom(ByteString data) throws InvalidProtocolBufferException {
      return (AuthenticateContinue)PARSER.parseFrom(data);
    }
    
    public static AuthenticateContinue parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (AuthenticateContinue)PARSER.parseFrom(data, extensionRegistry);
    }
    
    public static AuthenticateContinue parseFrom(byte[] data) throws InvalidProtocolBufferException {
      return (AuthenticateContinue)PARSER.parseFrom(data);
    }
    
    public static AuthenticateContinue parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (AuthenticateContinue)PARSER.parseFrom(data, extensionRegistry);
    }
    
    public static AuthenticateContinue parseFrom(InputStream input) throws IOException {
      return 
        (AuthenticateContinue)GeneratedMessageV3.parseWithIOException(PARSER, input);
    }
    
    public static AuthenticateContinue parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return 
        (AuthenticateContinue)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
    }
    
    public static AuthenticateContinue parseDelimitedFrom(InputStream input) throws IOException {
      return 
        (AuthenticateContinue)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
    }
    
    public static AuthenticateContinue parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return 
        (AuthenticateContinue)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    
    public static AuthenticateContinue parseFrom(CodedInputStream input) throws IOException {
      return 
        (AuthenticateContinue)GeneratedMessageV3.parseWithIOException(PARSER, input);
    }
    
    public static AuthenticateContinue parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return 
        (AuthenticateContinue)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
    }
    
    public Builder newBuilderForType() {
      return newBuilder();
    }
    
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    
    public static Builder newBuilder(AuthenticateContinue prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    
    public Builder toBuilder() {
      return (this == DEFAULT_INSTANCE) ? new Builder() : (new Builder())
        .mergeFrom(this);
    }
    
    protected Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    
    public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements MysqlxSession.AuthenticateContinueOrBuilder {
      private int bitField0_;
      
      private ByteString authData_;
      
      public static final Descriptors.Descriptor getDescriptor() {
        return MysqlxSession.internal_static_Mysqlx_Session_AuthenticateContinue_descriptor;
      }
      
      protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
        return MysqlxSession.internal_static_Mysqlx_Session_AuthenticateContinue_fieldAccessorTable
          .ensureFieldAccessorsInitialized(MysqlxSession.AuthenticateContinue.class, Builder.class);
      }
      
      private Builder() {
        this.authData_ = ByteString.EMPTY;
        maybeForceBuilderInitialization();
      }
      
      private Builder(GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        this.authData_ = ByteString.EMPTY;
        maybeForceBuilderInitialization();
      }
      
      private void maybeForceBuilderInitialization() {
        if (MysqlxSession.AuthenticateContinue.alwaysUseFieldBuilders);
      }
      
      public Builder clear() {
        super.clear();
        this.authData_ = ByteString.EMPTY;
        this.bitField0_ &= 0xFFFFFFFE;
        return this;
      }
      
      public Descriptors.Descriptor getDescriptorForType() {
        return MysqlxSession.internal_static_Mysqlx_Session_AuthenticateContinue_descriptor;
      }
      
      public MysqlxSession.AuthenticateContinue getDefaultInstanceForType() {
        return MysqlxSession.AuthenticateContinue.getDefaultInstance();
      }
      
      public MysqlxSession.AuthenticateContinue build() {
        MysqlxSession.AuthenticateContinue result = buildPartial();
        if (!result.isInitialized())
          throw newUninitializedMessageException(result); 
        return result;
      }
      
      public MysqlxSession.AuthenticateContinue buildPartial() {
        MysqlxSession.AuthenticateContinue result = new MysqlxSession.AuthenticateContinue(this);
        int from_bitField0_ = this.bitField0_;
        int to_bitField0_ = 0;
        if ((from_bitField0_ & 0x1) == 1)
          to_bitField0_ |= 0x1; 
        result.authData_ = this.authData_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder clone() {
        return (Builder)super.clone();
      }
      
      public Builder setField(Descriptors.FieldDescriptor field, Object value) {
        return (Builder)super.setField(field, value);
      }
      
      public Builder clearField(Descriptors.FieldDescriptor field) {
        return (Builder)super.clearField(field);
      }
      
      public Builder clearOneof(Descriptors.OneofDescriptor oneof) {
        return (Builder)super.clearOneof(oneof);
      }
      
      public Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
        return (Builder)super.setRepeatedField(field, index, value);
      }
      
      public Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
        return (Builder)super.addRepeatedField(field, value);
      }
      
      public Builder mergeFrom(Message other) {
        if (other instanceof MysqlxSession.AuthenticateContinue)
          return mergeFrom((MysqlxSession.AuthenticateContinue)other); 
        super.mergeFrom(other);
        return this;
      }
      
      public Builder mergeFrom(MysqlxSession.AuthenticateContinue other) {
        if (other == MysqlxSession.AuthenticateContinue.getDefaultInstance())
          return this; 
        if (other.hasAuthData())
          setAuthData(other.getAuthData()); 
        mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasAuthData())
          return false; 
        return true;
      }
      
      public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        MysqlxSession.AuthenticateContinue parsedMessage = null;
        try {
          parsedMessage = (MysqlxSession.AuthenticateContinue)MysqlxSession.AuthenticateContinue.PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (InvalidProtocolBufferException e) {
          parsedMessage = (MysqlxSession.AuthenticateContinue)e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null)
            mergeFrom(parsedMessage); 
        } 
        return this;
      }
      
      public boolean hasAuthData() {
        return ((this.bitField0_ & 0x1) == 1);
      }
      
      public ByteString getAuthData() {
        return this.authData_;
      }
      
      public Builder setAuthData(ByteString value) {
        if (value == null)
          throw new NullPointerException(); 
        this.bitField0_ |= 0x1;
        this.authData_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearAuthData() {
        this.bitField0_ &= 0xFFFFFFFE;
        this.authData_ = MysqlxSession.AuthenticateContinue.getDefaultInstance().getAuthData();
        onChanged();
        return this;
      }
      
      public final Builder setUnknownFields(UnknownFieldSet unknownFields) {
        return (Builder)super.setUnknownFields(unknownFields);
      }
      
      public final Builder mergeUnknownFields(UnknownFieldSet unknownFields) {
        return (Builder)super.mergeUnknownFields(unknownFields);
      }
    }
    
    private static final AuthenticateContinue DEFAULT_INSTANCE = new AuthenticateContinue();
    
    public static AuthenticateContinue getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }
    
    @Deprecated
    public static final Parser<AuthenticateContinue> PARSER = (Parser<AuthenticateContinue>)new AbstractParser<AuthenticateContinue>() {
        public MysqlxSession.AuthenticateContinue parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
          return new MysqlxSession.AuthenticateContinue(input, extensionRegistry);
        }
      };
    
    public static Parser<AuthenticateContinue> parser() {
      return PARSER;
    }
    
    public Parser<AuthenticateContinue> getParserForType() {
      return PARSER;
    }
    
    public AuthenticateContinue getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }
  }
  
  public static final class AuthenticateOk extends GeneratedMessageV3 implements AuthenticateOkOrBuilder {
    private static final long serialVersionUID = 0L;
    
    private int bitField0_;
    
    public static final int AUTH_DATA_FIELD_NUMBER = 1;
    
    private ByteString authData_;
    
    private byte memoizedIsInitialized;
    
    private AuthenticateOk(GeneratedMessageV3.Builder<?> builder) {
      super(builder);
      this.memoizedIsInitialized = -1;
    }
    
    private AuthenticateOk() {
      this.memoizedIsInitialized = -1;
      this.authData_ = ByteString.EMPTY;
    }
    
    public final UnknownFieldSet getUnknownFields() {
      return this.unknownFields;
    }
    
    private AuthenticateOk(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null)
        throw new NullPointerException(); 
      int mutable_bitField0_ = 0;
      UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              continue;
            case 10:
              this.bitField0_ |= 0x1;
              this.authData_ = input.readBytes();
              continue;
          } 
          if (!parseUnknownField(input, unknownFields, extensionRegistry, tag))
            done = true; 
        } 
      } catch (InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (IOException e) {
        throw (new InvalidProtocolBufferException(e)).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      } 
    }
    
    public static final Descriptors.Descriptor getDescriptor() {
      return MysqlxSession.internal_static_Mysqlx_Session_AuthenticateOk_descriptor;
    }
    
    protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
      return MysqlxSession.internal_static_Mysqlx_Session_AuthenticateOk_fieldAccessorTable.ensureFieldAccessorsInitialized(AuthenticateOk.class, Builder.class);
    }
    
    public boolean hasAuthData() {
      return ((this.bitField0_ & 0x1) == 1);
    }
    
    public ByteString getAuthData() {
      return this.authData_;
    }
    
    public final boolean isInitialized() {
      byte isInitialized = this.memoizedIsInitialized;
      if (isInitialized == 1)
        return true; 
      if (isInitialized == 0)
        return false; 
      this.memoizedIsInitialized = 1;
      return true;
    }
    
    public void writeTo(CodedOutputStream output) throws IOException {
      if ((this.bitField0_ & 0x1) == 1)
        output.writeBytes(1, this.authData_); 
      this.unknownFields.writeTo(output);
    }
    
    public int getSerializedSize() {
      int size = this.memoizedSize;
      if (size != -1)
        return size; 
      size = 0;
      if ((this.bitField0_ & 0x1) == 1)
        size += 
          CodedOutputStream.computeBytesSize(1, this.authData_); 
      size += this.unknownFields.getSerializedSize();
      this.memoizedSize = size;
      return size;
    }
    
    public boolean equals(Object obj) {
      if (obj == this)
        return true; 
      if (!(obj instanceof AuthenticateOk))
        return super.equals(obj); 
      AuthenticateOk other = (AuthenticateOk)obj;
      boolean result = true;
      result = (result && hasAuthData() == other.hasAuthData());
      if (hasAuthData())
        result = (result && getAuthData().equals(other.getAuthData())); 
      result = (result && this.unknownFields.equals(other.unknownFields));
      return result;
    }
    
    public int hashCode() {
      if (this.memoizedHashCode != 0)
        return this.memoizedHashCode; 
      int hash = 41;
      hash = 19 * hash + getDescriptor().hashCode();
      if (hasAuthData()) {
        hash = 37 * hash + 1;
        hash = 53 * hash + getAuthData().hashCode();
      } 
      hash = 29 * hash + this.unknownFields.hashCode();
      this.memoizedHashCode = hash;
      return hash;
    }
    
    public static AuthenticateOk parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
      return (AuthenticateOk)PARSER.parseFrom(data);
    }
    
    public static AuthenticateOk parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (AuthenticateOk)PARSER.parseFrom(data, extensionRegistry);
    }
    
    public static AuthenticateOk parseFrom(ByteString data) throws InvalidProtocolBufferException {
      return (AuthenticateOk)PARSER.parseFrom(data);
    }
    
    public static AuthenticateOk parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (AuthenticateOk)PARSER.parseFrom(data, extensionRegistry);
    }
    
    public static AuthenticateOk parseFrom(byte[] data) throws InvalidProtocolBufferException {
      return (AuthenticateOk)PARSER.parseFrom(data);
    }
    
    public static AuthenticateOk parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (AuthenticateOk)PARSER.parseFrom(data, extensionRegistry);
    }
    
    public static AuthenticateOk parseFrom(InputStream input) throws IOException {
      return 
        (AuthenticateOk)GeneratedMessageV3.parseWithIOException(PARSER, input);
    }
    
    public static AuthenticateOk parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return 
        (AuthenticateOk)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
    }
    
    public static AuthenticateOk parseDelimitedFrom(InputStream input) throws IOException {
      return 
        (AuthenticateOk)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
    }
    
    public static AuthenticateOk parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return 
        (AuthenticateOk)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    
    public static AuthenticateOk parseFrom(CodedInputStream input) throws IOException {
      return 
        (AuthenticateOk)GeneratedMessageV3.parseWithIOException(PARSER, input);
    }
    
    public static AuthenticateOk parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return 
        (AuthenticateOk)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
    }
    
    public Builder newBuilderForType() {
      return newBuilder();
    }
    
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    
    public static Builder newBuilder(AuthenticateOk prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    
    public Builder toBuilder() {
      return (this == DEFAULT_INSTANCE) ? new Builder() : (new Builder())
        .mergeFrom(this);
    }
    
    protected Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    
    public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements MysqlxSession.AuthenticateOkOrBuilder {
      private int bitField0_;
      
      private ByteString authData_;
      
      public static final Descriptors.Descriptor getDescriptor() {
        return MysqlxSession.internal_static_Mysqlx_Session_AuthenticateOk_descriptor;
      }
      
      protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
        return MysqlxSession.internal_static_Mysqlx_Session_AuthenticateOk_fieldAccessorTable
          .ensureFieldAccessorsInitialized(MysqlxSession.AuthenticateOk.class, Builder.class);
      }
      
      private Builder() {
        this.authData_ = ByteString.EMPTY;
        maybeForceBuilderInitialization();
      }
      
      private Builder(GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        this.authData_ = ByteString.EMPTY;
        maybeForceBuilderInitialization();
      }
      
      private void maybeForceBuilderInitialization() {
        if (MysqlxSession.AuthenticateOk.alwaysUseFieldBuilders);
      }
      
      public Builder clear() {
        super.clear();
        this.authData_ = ByteString.EMPTY;
        this.bitField0_ &= 0xFFFFFFFE;
        return this;
      }
      
      public Descriptors.Descriptor getDescriptorForType() {
        return MysqlxSession.internal_static_Mysqlx_Session_AuthenticateOk_descriptor;
      }
      
      public MysqlxSession.AuthenticateOk getDefaultInstanceForType() {
        return MysqlxSession.AuthenticateOk.getDefaultInstance();
      }
      
      public MysqlxSession.AuthenticateOk build() {
        MysqlxSession.AuthenticateOk result = buildPartial();
        if (!result.isInitialized())
          throw newUninitializedMessageException(result); 
        return result;
      }
      
      public MysqlxSession.AuthenticateOk buildPartial() {
        MysqlxSession.AuthenticateOk result = new MysqlxSession.AuthenticateOk(this);
        int from_bitField0_ = this.bitField0_;
        int to_bitField0_ = 0;
        if ((from_bitField0_ & 0x1) == 1)
          to_bitField0_ |= 0x1; 
        result.authData_ = this.authData_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder clone() {
        return (Builder)super.clone();
      }
      
      public Builder setField(Descriptors.FieldDescriptor field, Object value) {
        return (Builder)super.setField(field, value);
      }
      
      public Builder clearField(Descriptors.FieldDescriptor field) {
        return (Builder)super.clearField(field);
      }
      
      public Builder clearOneof(Descriptors.OneofDescriptor oneof) {
        return (Builder)super.clearOneof(oneof);
      }
      
      public Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
        return (Builder)super.setRepeatedField(field, index, value);
      }
      
      public Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
        return (Builder)super.addRepeatedField(field, value);
      }
      
      public Builder mergeFrom(Message other) {
        if (other instanceof MysqlxSession.AuthenticateOk)
          return mergeFrom((MysqlxSession.AuthenticateOk)other); 
        super.mergeFrom(other);
        return this;
      }
      
      public Builder mergeFrom(MysqlxSession.AuthenticateOk other) {
        if (other == MysqlxSession.AuthenticateOk.getDefaultInstance())
          return this; 
        if (other.hasAuthData())
          setAuthData(other.getAuthData()); 
        mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }
      
      public final boolean isInitialized() {
        return true;
      }
      
      public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        MysqlxSession.AuthenticateOk parsedMessage = null;
        try {
          parsedMessage = (MysqlxSession.AuthenticateOk)MysqlxSession.AuthenticateOk.PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (InvalidProtocolBufferException e) {
          parsedMessage = (MysqlxSession.AuthenticateOk)e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null)
            mergeFrom(parsedMessage); 
        } 
        return this;
      }
      
      public boolean hasAuthData() {
        return ((this.bitField0_ & 0x1) == 1);
      }
      
      public ByteString getAuthData() {
        return this.authData_;
      }
      
      public Builder setAuthData(ByteString value) {
        if (value == null)
          throw new NullPointerException(); 
        this.bitField0_ |= 0x1;
        this.authData_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearAuthData() {
        this.bitField0_ &= 0xFFFFFFFE;
        this.authData_ = MysqlxSession.AuthenticateOk.getDefaultInstance().getAuthData();
        onChanged();
        return this;
      }
      
      public final Builder setUnknownFields(UnknownFieldSet unknownFields) {
        return (Builder)super.setUnknownFields(unknownFields);
      }
      
      public final Builder mergeUnknownFields(UnknownFieldSet unknownFields) {
        return (Builder)super.mergeUnknownFields(unknownFields);
      }
    }
    
    private static final AuthenticateOk DEFAULT_INSTANCE = new AuthenticateOk();
    
    public static AuthenticateOk getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }
    
    @Deprecated
    public static final Parser<AuthenticateOk> PARSER = (Parser<AuthenticateOk>)new AbstractParser<AuthenticateOk>() {
        public MysqlxSession.AuthenticateOk parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
          return new MysqlxSession.AuthenticateOk(input, extensionRegistry);
        }
      };
    
    public static Parser<AuthenticateOk> parser() {
      return PARSER;
    }
    
    public Parser<AuthenticateOk> getParserForType() {
      return PARSER;
    }
    
    public AuthenticateOk getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }
  }
  
  public static final class Reset extends GeneratedMessageV3 implements ResetOrBuilder {
    private static final long serialVersionUID = 0L;
    
    private int bitField0_;
    
    public static final int KEEP_OPEN_FIELD_NUMBER = 1;
    
    private boolean keepOpen_;
    
    private byte memoizedIsInitialized;
    
    private Reset(GeneratedMessageV3.Builder<?> builder) {
      super(builder);
      this.memoizedIsInitialized = -1;
    }
    
    private Reset() {
      this.memoizedIsInitialized = -1;
      this.keepOpen_ = false;
    }
    
    public final UnknownFieldSet getUnknownFields() {
      return this.unknownFields;
    }
    
    private Reset(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null)
        throw new NullPointerException(); 
      int mutable_bitField0_ = 0;
      UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              continue;
            case 8:
              this.bitField0_ |= 0x1;
              this.keepOpen_ = input.readBool();
              continue;
          } 
          if (!parseUnknownField(input, unknownFields, extensionRegistry, tag))
            done = true; 
        } 
      } catch (InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (IOException e) {
        throw (new InvalidProtocolBufferException(e)).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      } 
    }
    
    public static final Descriptors.Descriptor getDescriptor() {
      return MysqlxSession.internal_static_Mysqlx_Session_Reset_descriptor;
    }
    
    protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
      return MysqlxSession.internal_static_Mysqlx_Session_Reset_fieldAccessorTable.ensureFieldAccessorsInitialized(Reset.class, Builder.class);
    }
    
    public boolean hasKeepOpen() {
      return ((this.bitField0_ & 0x1) == 1);
    }
    
    public boolean getKeepOpen() {
      return this.keepOpen_;
    }
    
    public final boolean isInitialized() {
      byte isInitialized = this.memoizedIsInitialized;
      if (isInitialized == 1)
        return true; 
      if (isInitialized == 0)
        return false; 
      this.memoizedIsInitialized = 1;
      return true;
    }
    
    public void writeTo(CodedOutputStream output) throws IOException {
      if ((this.bitField0_ & 0x1) == 1)
        output.writeBool(1, this.keepOpen_); 
      this.unknownFields.writeTo(output);
    }
    
    public int getSerializedSize() {
      int size = this.memoizedSize;
      if (size != -1)
        return size; 
      size = 0;
      if ((this.bitField0_ & 0x1) == 1)
        size += 
          CodedOutputStream.computeBoolSize(1, this.keepOpen_); 
      size += this.unknownFields.getSerializedSize();
      this.memoizedSize = size;
      return size;
    }
    
    public boolean equals(Object obj) {
      if (obj == this)
        return true; 
      if (!(obj instanceof Reset))
        return super.equals(obj); 
      Reset other = (Reset)obj;
      boolean result = true;
      result = (result && hasKeepOpen() == other.hasKeepOpen());
      if (hasKeepOpen())
        result = (result && getKeepOpen() == other.getKeepOpen()); 
      result = (result && this.unknownFields.equals(other.unknownFields));
      return result;
    }
    
    public int hashCode() {
      if (this.memoizedHashCode != 0)
        return this.memoizedHashCode; 
      int hash = 41;
      hash = 19 * hash + getDescriptor().hashCode();
      if (hasKeepOpen()) {
        hash = 37 * hash + 1;
        hash = 53 * hash + Internal.hashBoolean(
            getKeepOpen());
      } 
      hash = 29 * hash + this.unknownFields.hashCode();
      this.memoizedHashCode = hash;
      return hash;
    }
    
    public static Reset parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
      return (Reset)PARSER.parseFrom(data);
    }
    
    public static Reset parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (Reset)PARSER.parseFrom(data, extensionRegistry);
    }
    
    public static Reset parseFrom(ByteString data) throws InvalidProtocolBufferException {
      return (Reset)PARSER.parseFrom(data);
    }
    
    public static Reset parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (Reset)PARSER.parseFrom(data, extensionRegistry);
    }
    
    public static Reset parseFrom(byte[] data) throws InvalidProtocolBufferException {
      return (Reset)PARSER.parseFrom(data);
    }
    
    public static Reset parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (Reset)PARSER.parseFrom(data, extensionRegistry);
    }
    
    public static Reset parseFrom(InputStream input) throws IOException {
      return 
        (Reset)GeneratedMessageV3.parseWithIOException(PARSER, input);
    }
    
    public static Reset parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return 
        (Reset)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
    }
    
    public static Reset parseDelimitedFrom(InputStream input) throws IOException {
      return 
        (Reset)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
    }
    
    public static Reset parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return 
        (Reset)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    
    public static Reset parseFrom(CodedInputStream input) throws IOException {
      return 
        (Reset)GeneratedMessageV3.parseWithIOException(PARSER, input);
    }
    
    public static Reset parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return 
        (Reset)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
    }
    
    public Builder newBuilderForType() {
      return newBuilder();
    }
    
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    
    public static Builder newBuilder(Reset prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    
    public Builder toBuilder() {
      return (this == DEFAULT_INSTANCE) ? new Builder() : (new Builder())
        .mergeFrom(this);
    }
    
    protected Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    
    public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements MysqlxSession.ResetOrBuilder {
      private int bitField0_;
      
      private boolean keepOpen_;
      
      public static final Descriptors.Descriptor getDescriptor() {
        return MysqlxSession.internal_static_Mysqlx_Session_Reset_descriptor;
      }
      
      protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
        return MysqlxSession.internal_static_Mysqlx_Session_Reset_fieldAccessorTable
          .ensureFieldAccessorsInitialized(MysqlxSession.Reset.class, Builder.class);
      }
      
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      
      private void maybeForceBuilderInitialization() {
        if (MysqlxSession.Reset.alwaysUseFieldBuilders);
      }
      
      public Builder clear() {
        super.clear();
        this.keepOpen_ = false;
        this.bitField0_ &= 0xFFFFFFFE;
        return this;
      }
      
      public Descriptors.Descriptor getDescriptorForType() {
        return MysqlxSession.internal_static_Mysqlx_Session_Reset_descriptor;
      }
      
      public MysqlxSession.Reset getDefaultInstanceForType() {
        return MysqlxSession.Reset.getDefaultInstance();
      }
      
      public MysqlxSession.Reset build() {
        MysqlxSession.Reset result = buildPartial();
        if (!result.isInitialized())
          throw newUninitializedMessageException(result); 
        return result;
      }
      
      public MysqlxSession.Reset buildPartial() {
        MysqlxSession.Reset result = new MysqlxSession.Reset(this);
        int from_bitField0_ = this.bitField0_;
        int to_bitField0_ = 0;
        if ((from_bitField0_ & 0x1) == 1)
          to_bitField0_ |= 0x1; 
        result.keepOpen_ = this.keepOpen_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder clone() {
        return (Builder)super.clone();
      }
      
      public Builder setField(Descriptors.FieldDescriptor field, Object value) {
        return (Builder)super.setField(field, value);
      }
      
      public Builder clearField(Descriptors.FieldDescriptor field) {
        return (Builder)super.clearField(field);
      }
      
      public Builder clearOneof(Descriptors.OneofDescriptor oneof) {
        return (Builder)super.clearOneof(oneof);
      }
      
      public Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
        return (Builder)super.setRepeatedField(field, index, value);
      }
      
      public Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
        return (Builder)super.addRepeatedField(field, value);
      }
      
      public Builder mergeFrom(Message other) {
        if (other instanceof MysqlxSession.Reset)
          return mergeFrom((MysqlxSession.Reset)other); 
        super.mergeFrom(other);
        return this;
      }
      
      public Builder mergeFrom(MysqlxSession.Reset other) {
        if (other == MysqlxSession.Reset.getDefaultInstance())
          return this; 
        if (other.hasKeepOpen())
          setKeepOpen(other.getKeepOpen()); 
        mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }
      
      public final boolean isInitialized() {
        return true;
      }
      
      public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        MysqlxSession.Reset parsedMessage = null;
        try {
          parsedMessage = (MysqlxSession.Reset)MysqlxSession.Reset.PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (InvalidProtocolBufferException e) {
          parsedMessage = (MysqlxSession.Reset)e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null)
            mergeFrom(parsedMessage); 
        } 
        return this;
      }
      
      public boolean hasKeepOpen() {
        return ((this.bitField0_ & 0x1) == 1);
      }
      
      public boolean getKeepOpen() {
        return this.keepOpen_;
      }
      
      public Builder setKeepOpen(boolean value) {
        this.bitField0_ |= 0x1;
        this.keepOpen_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearKeepOpen() {
        this.bitField0_ &= 0xFFFFFFFE;
        this.keepOpen_ = false;
        onChanged();
        return this;
      }
      
      public final Builder setUnknownFields(UnknownFieldSet unknownFields) {
        return (Builder)super.setUnknownFields(unknownFields);
      }
      
      public final Builder mergeUnknownFields(UnknownFieldSet unknownFields) {
        return (Builder)super.mergeUnknownFields(unknownFields);
      }
    }
    
    private static final Reset DEFAULT_INSTANCE = new Reset();
    
    public static Reset getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }
    
    @Deprecated
    public static final Parser<Reset> PARSER = (Parser<Reset>)new AbstractParser<Reset>() {
        public MysqlxSession.Reset parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
          return new MysqlxSession.Reset(input, extensionRegistry);
        }
      };
    
    public static Parser<Reset> parser() {
      return PARSER;
    }
    
    public Parser<Reset> getParserForType() {
      return PARSER;
    }
    
    public Reset getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }
  }
  
  public static final class Close extends GeneratedMessageV3 implements CloseOrBuilder {
    private static final long serialVersionUID = 0L;
    
    private byte memoizedIsInitialized;
    
    private Close(GeneratedMessageV3.Builder<?> builder) {
      super(builder);
      this.memoizedIsInitialized = -1;
    }
    
    private Close() {
      this.memoizedIsInitialized = -1;
    }
    
    public final UnknownFieldSet getUnknownFields() {
      return this.unknownFields;
    }
    
    private Close(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null)
        throw new NullPointerException(); 
      UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              continue;
          } 
          if (!parseUnknownField(input, unknownFields, extensionRegistry, tag))
            done = true; 
        } 
      } catch (InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (IOException e) {
        throw (new InvalidProtocolBufferException(e)).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      } 
    }
    
    public static final Descriptors.Descriptor getDescriptor() {
      return MysqlxSession.internal_static_Mysqlx_Session_Close_descriptor;
    }
    
    protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
      return MysqlxSession.internal_static_Mysqlx_Session_Close_fieldAccessorTable.ensureFieldAccessorsInitialized(Close.class, Builder.class);
    }
    
    public final boolean isInitialized() {
      byte isInitialized = this.memoizedIsInitialized;
      if (isInitialized == 1)
        return true; 
      if (isInitialized == 0)
        return false; 
      this.memoizedIsInitialized = 1;
      return true;
    }
    
    public void writeTo(CodedOutputStream output) throws IOException {
      this.unknownFields.writeTo(output);
    }
    
    public int getSerializedSize() {
      int size = this.memoizedSize;
      if (size != -1)
        return size; 
      size = 0;
      size += this.unknownFields.getSerializedSize();
      this.memoizedSize = size;
      return size;
    }
    
    public boolean equals(Object obj) {
      if (obj == this)
        return true; 
      if (!(obj instanceof Close))
        return super.equals(obj); 
      Close other = (Close)obj;
      boolean result = true;
      result = (result && this.unknownFields.equals(other.unknownFields));
      return result;
    }
    
    public int hashCode() {
      if (this.memoizedHashCode != 0)
        return this.memoizedHashCode; 
      int hash = 41;
      hash = 19 * hash + getDescriptor().hashCode();
      hash = 29 * hash + this.unknownFields.hashCode();
      this.memoizedHashCode = hash;
      return hash;
    }
    
    public static Close parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
      return (Close)PARSER.parseFrom(data);
    }
    
    public static Close parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (Close)PARSER.parseFrom(data, extensionRegistry);
    }
    
    public static Close parseFrom(ByteString data) throws InvalidProtocolBufferException {
      return (Close)PARSER.parseFrom(data);
    }
    
    public static Close parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (Close)PARSER.parseFrom(data, extensionRegistry);
    }
    
    public static Close parseFrom(byte[] data) throws InvalidProtocolBufferException {
      return (Close)PARSER.parseFrom(data);
    }
    
    public static Close parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (Close)PARSER.parseFrom(data, extensionRegistry);
    }
    
    public static Close parseFrom(InputStream input) throws IOException {
      return 
        (Close)GeneratedMessageV3.parseWithIOException(PARSER, input);
    }
    
    public static Close parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return 
        (Close)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
    }
    
    public static Close parseDelimitedFrom(InputStream input) throws IOException {
      return 
        (Close)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
    }
    
    public static Close parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return 
        (Close)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    
    public static Close parseFrom(CodedInputStream input) throws IOException {
      return 
        (Close)GeneratedMessageV3.parseWithIOException(PARSER, input);
    }
    
    public static Close parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return 
        (Close)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
    }
    
    public Builder newBuilderForType() {
      return newBuilder();
    }
    
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    
    public static Builder newBuilder(Close prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    
    public Builder toBuilder() {
      return (this == DEFAULT_INSTANCE) ? new Builder() : (new Builder())
        .mergeFrom(this);
    }
    
    protected Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    
    public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements MysqlxSession.CloseOrBuilder {
      public static final Descriptors.Descriptor getDescriptor() {
        return MysqlxSession.internal_static_Mysqlx_Session_Close_descriptor;
      }
      
      protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
        return MysqlxSession.internal_static_Mysqlx_Session_Close_fieldAccessorTable
          .ensureFieldAccessorsInitialized(MysqlxSession.Close.class, Builder.class);
      }
      
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      
      private void maybeForceBuilderInitialization() {
        if (MysqlxSession.Close.alwaysUseFieldBuilders);
      }
      
      public Builder clear() {
        super.clear();
        return this;
      }
      
      public Descriptors.Descriptor getDescriptorForType() {
        return MysqlxSession.internal_static_Mysqlx_Session_Close_descriptor;
      }
      
      public MysqlxSession.Close getDefaultInstanceForType() {
        return MysqlxSession.Close.getDefaultInstance();
      }
      
      public MysqlxSession.Close build() {
        MysqlxSession.Close result = buildPartial();
        if (!result.isInitialized())
          throw newUninitializedMessageException(result); 
        return result;
      }
      
      public MysqlxSession.Close buildPartial() {
        MysqlxSession.Close result = new MysqlxSession.Close(this);
        onBuilt();
        return result;
      }
      
      public Builder clone() {
        return (Builder)super.clone();
      }
      
      public Builder setField(Descriptors.FieldDescriptor field, Object value) {
        return (Builder)super.setField(field, value);
      }
      
      public Builder clearField(Descriptors.FieldDescriptor field) {
        return (Builder)super.clearField(field);
      }
      
      public Builder clearOneof(Descriptors.OneofDescriptor oneof) {
        return (Builder)super.clearOneof(oneof);
      }
      
      public Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
        return (Builder)super.setRepeatedField(field, index, value);
      }
      
      public Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
        return (Builder)super.addRepeatedField(field, value);
      }
      
      public Builder mergeFrom(Message other) {
        if (other instanceof MysqlxSession.Close)
          return mergeFrom((MysqlxSession.Close)other); 
        super.mergeFrom(other);
        return this;
      }
      
      public Builder mergeFrom(MysqlxSession.Close other) {
        if (other == MysqlxSession.Close.getDefaultInstance())
          return this; 
        mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }
      
      public final boolean isInitialized() {
        return true;
      }
      
      public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        MysqlxSession.Close parsedMessage = null;
        try {
          parsedMessage = (MysqlxSession.Close)MysqlxSession.Close.PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (InvalidProtocolBufferException e) {
          parsedMessage = (MysqlxSession.Close)e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null)
            mergeFrom(parsedMessage); 
        } 
        return this;
      }
      
      public final Builder setUnknownFields(UnknownFieldSet unknownFields) {
        return (Builder)super.setUnknownFields(unknownFields);
      }
      
      public final Builder mergeUnknownFields(UnknownFieldSet unknownFields) {
        return (Builder)super.mergeUnknownFields(unknownFields);
      }
    }
    
    private static final Close DEFAULT_INSTANCE = new Close();
    
    public static Close getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }
    
    @Deprecated
    public static final Parser<Close> PARSER = (Parser<Close>)new AbstractParser<Close>() {
        public MysqlxSession.Close parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
          return new MysqlxSession.Close(input, extensionRegistry);
        }
      };
    
    public static Parser<Close> parser() {
      return PARSER;
    }
    
    public Parser<Close> getParserForType() {
      return PARSER;
    }
    
    public Close getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }
  }
  
  public static Descriptors.FileDescriptor getDescriptor() {
    return descriptor;
  }
  
  static {
    String[] descriptorData = { "\n\024mysqlx_session.proto\022\016Mysqlx.Session\032\fmysqlx.proto\"Y\n\021AuthenticateStart\022\021\n\tmech_name\030\001 \002(\t\022\021\n\tauth_data\030\002 \001(\f\022\030\n\020initial_response\030\003 \001(\f:\004ê0\004\"3\n\024AuthenticateContinue\022\021\n\tauth_data\030\001 \002(\f:\bê0\003ê0\005\")\n\016AuthenticateOk\022\021\n\tauth_data\030\001 \001(\f:\004ê0\004\"'\n\005Reset\022\030\n\tkeep_open\030\001 \001(\b:\005false:\004ê0\006\"\r\n\005Close:\004ê0\007B\031\n\027com.mysql.cj.x.protobuf" };
    Descriptors.FileDescriptor.InternalDescriptorAssigner assigner = new Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public ExtensionRegistry assignDescriptors(Descriptors.FileDescriptor root) {
          MysqlxSession.descriptor = root;
          return null;
        }
      };
    Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(descriptorData, new Descriptors.FileDescriptor[] { Mysqlx.getDescriptor() }, assigner);
  }
  
  private static final Descriptors.Descriptor internal_static_Mysqlx_Session_AuthenticateStart_descriptor = getDescriptor().getMessageTypes().get(0);
  
  private static final GeneratedMessageV3.FieldAccessorTable internal_static_Mysqlx_Session_AuthenticateStart_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_Mysqlx_Session_AuthenticateStart_descriptor, new String[] { "MechName", "AuthData", "InitialResponse" });
  
  private static final Descriptors.Descriptor internal_static_Mysqlx_Session_AuthenticateContinue_descriptor = getDescriptor().getMessageTypes().get(1);
  
  private static final GeneratedMessageV3.FieldAccessorTable internal_static_Mysqlx_Session_AuthenticateContinue_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_Mysqlx_Session_AuthenticateContinue_descriptor, new String[] { "AuthData" });
  
  private static final Descriptors.Descriptor internal_static_Mysqlx_Session_AuthenticateOk_descriptor = getDescriptor().getMessageTypes().get(2);
  
  private static final GeneratedMessageV3.FieldAccessorTable internal_static_Mysqlx_Session_AuthenticateOk_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_Mysqlx_Session_AuthenticateOk_descriptor, new String[] { "AuthData" });
  
  private static final Descriptors.Descriptor internal_static_Mysqlx_Session_Reset_descriptor = getDescriptor().getMessageTypes().get(3);
  
  private static final GeneratedMessageV3.FieldAccessorTable internal_static_Mysqlx_Session_Reset_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_Mysqlx_Session_Reset_descriptor, new String[] { "KeepOpen" });
  
  private static final Descriptors.Descriptor internal_static_Mysqlx_Session_Close_descriptor = getDescriptor().getMessageTypes().get(4);
  
  private static final GeneratedMessageV3.FieldAccessorTable internal_static_Mysqlx_Session_Close_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_Mysqlx_Session_Close_descriptor, new String[0]);
  
  private static Descriptors.FileDescriptor descriptor;
  
  static {
    ExtensionRegistry registry = ExtensionRegistry.newInstance();
    registry.add(Mysqlx.clientMessageId);
    registry.add(Mysqlx.serverMessageId);
    Descriptors.FileDescriptor.internalUpdateFileDescriptor(descriptor, registry);
    Mysqlx.getDescriptor();
  }
  
  public static interface CloseOrBuilder extends MessageOrBuilder {}
  
  public static interface ResetOrBuilder extends MessageOrBuilder {
    boolean hasKeepOpen();
    
    boolean getKeepOpen();
  }
  
  public static interface AuthenticateOkOrBuilder extends MessageOrBuilder {
    boolean hasAuthData();
    
    ByteString getAuthData();
  }
  
  public static interface AuthenticateContinueOrBuilder extends MessageOrBuilder {
    boolean hasAuthData();
    
    ByteString getAuthData();
  }
  
  public static interface AuthenticateStartOrBuilder extends MessageOrBuilder {
    boolean hasMechName();
    
    String getMechName();
    
    ByteString getMechNameBytes();
    
    boolean hasAuthData();
    
    ByteString getAuthData();
    
    boolean hasInitialResponse();
    
    ByteString getInitialResponse();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\x\protobuf\MysqlxSession.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */